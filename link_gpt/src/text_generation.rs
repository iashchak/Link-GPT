/*
 * MIT License
 *
 * Copyright (c) 2023 Andrei Iashchak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

use anyhow::{Error as E, Result};
use candle_core::{Device, DType, Tensor};
use candle_transformers::generation::LogitsProcessor;
use candle_transformers::models::mistral::Config;
use candle_transformers::models::quantized_mistral::Model as QMistral;
use hf_hub::{api::sync::Api, Repo, RepoType};
use indicatif::{ProgressIterator, ProgressStyle};
use tokenizers::Tokenizer;

use crate::tokenizer_output_stream::TokenOutputStream;

pub struct TextGeneration {
    model: QMistral,
    device: Device,
    tokenizer: TokenOutputStream,
    logits_processor: LogitsProcessor,
}

impl TextGeneration {
    pub fn new(model_id: &str) -> Result<Self> {
        let seed = 299792458;
        let temperature = Some(0.8);
        let top_p = None;

        let (model, tokenizer) =
            TextGeneration::get_model_and_tokenizer(model_id).map_err(E::msg)?;
        let device = Device::Cpu;
        let logits_processor = LogitsProcessor::new(seed, temperature, top_p);

        return Ok(Self {
            model,
            tokenizer: TokenOutputStream::new(tokenizer),
            logits_processor,
            device,
        });
    }

    fn get_model_and_tokenizer(model_id: &str) -> Result<(QMistral, Tokenizer)> {
        let revision = "main".to_string();
        let use_flash_attn = false;

        let api = Api::new()?;
        let repo = api.repo(Repo::with_revision(
            model_id.parse().unwrap(),
            RepoType::Model,
            revision,
        ));
        let tokenizer_filename = repo.get("tokenizer.json")?;
        let model_file = repo.get("model-q4k.gguf").map_err(|e| anyhow::anyhow!(e))?;
        let tokenizer = Tokenizer::from_file(tokenizer_filename).map_err(|e| anyhow::anyhow!(e))?;

        let config = Config::config_7b_v0_1(use_flash_attn).with_custom_vocab_size(32002);
        let vb = candle_transformers::quantized_var_builder::VarBuilder::from_gguf(model_file)
            .map_err(|e| anyhow::anyhow!(e))?;
        let model = QMistral::new(&config, vb).map_err(|e| anyhow::anyhow!(e))?;

        Ok((model, tokenizer))
    }

    pub fn run(&mut self, prompt: &str, sample_len: usize) -> Result<String> {
        self.tokenizer.clear();
        let prompt_with_suffix = prompt.to_string() + "\n<|im_start|>";
        let stop_sequence = "<|im_end|>";
        let mut tokens = self
            .tokenizer
            .tokenizer()
            .encode(prompt_with_suffix, true)
            .map_err(E::msg)?
            .get_ids()
            .to_vec();
        for &t in tokens.iter() {
            self.tokenizer.next_token(t)?;
        }
        let eos_token = match self.tokenizer.get_token("</s>") {
            Some(token) => token,
            None => anyhow::bail!("cannot find the </s> token"),
        };
        let mut generated_text = String::new();
        for index in (0..sample_len)
            .progress()
            .with_style(ProgressStyle::default_bar())
        {
            let context_size = if index > 0 { 1 } else { tokens.len() };
            let start_pos = tokens.len().saturating_sub(context_size);
            let ctxt = &tokens[start_pos..];
            let input = Tensor::new(ctxt, &self.device)?.unsqueeze(0)?;
            let logits = self.model.forward(&input, start_pos)?;
            let logits = logits.squeeze(0)?.squeeze(0)?.to_dtype(DType::F32)?;
            let next_token = self.logits_processor.sample(&logits)?;
            tokens.push(next_token);
            if next_token == eos_token {
                break;
            }

            if let Some(t) = self.tokenizer.next_token(next_token)? {
                generated_text.push_str(&t);
            }
            if generated_text.contains(stop_sequence) {
                break;
            }
        }
        if let Some(rest) = self.tokenizer.decode_rest().map_err(E::msg)? {
            generated_text.push_str(&rest);
        }
        if let Some(pos) = generated_text.find(stop_sequence) {
            generated_text.truncate(pos);
        }
        Ok(generated_text)
    }
}
