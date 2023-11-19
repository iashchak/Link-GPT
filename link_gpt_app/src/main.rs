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

use std::io::Write;
use anyhow::Result;

use link_gpt::dialog::Dialog;
use link_gpt::text_generation::TextGeneration;

fn main() -> Result<()> {
    let mut pipeline = TextGeneration::new("iashchak/link-gpt-7b")?;
    let mut dialog = Dialog::new().with_system();
    dialog
        .add_message(("user1", "Привет! Как поживаешь?").into())
        .add_message(("user2", "Та хуй знает").into())
        .add_message(("user1", "Что про путина думаешь?").into())
        .add_message(("user2", "Пиздец").into());
    loop {
        println!("{}", dialog.to_string());
        print!("Введите сообщение: ");
        std::io::stdout().flush().unwrap();
        let mut line = String::new();
        std::io::stdin().read_line(&mut line).expect("Failed to read line");
        if line.is_empty() {
            break;
        }
        dialog.add_message(("user1", line.trim()).into());
        let mut prompt = dialog.to_string();
        prompt.push_str("<|im_start|>user2\n");
        let result = pipeline.run(&prompt, 64)?;
        println!("{}", result);
        dialog.add_message(("user2", result.as_str()).into());
    }
    Ok(())
}
