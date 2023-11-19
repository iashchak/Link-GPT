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

package eu.iashchak.linkgpt;

import eu.iashchak.linkgpt.utils.LinkGPTLoader;

/**
 * This class serves as a Java binding to the Rust 'TextGeneration' struct,
 * facilitating text generation using a specified model.
 */
public class TextGeneration {
    // Load the native library containing the Rust Text Generation implementation
    static {
        LinkGPTLoader.load();
    }

    // Pointer to the native Rust TextGeneration structure
    private final long ptr;

    /**
     * Constructor that initializes the TextGeneration instance.
     *
     * @param model_path The path to the text generation model. This path is
     *                   used by the native Rust code to initialize the model.
     */
    public TextGeneration(String model_path) {
        ptr = new_text_generation(model_path);
    }

    /**
     * Native method that constructs a new TextGeneration instance in Rust.
     * This method is declared native as it interacts with Rust code.
     *
     * @param model_path The path to the model used for text generation.
     * @return A long value representing a pointer to the native Rust TextGeneration structure.
     */
    private native long new_text_generation(String model_path);

    /**
     * Native method to generate text using the Rust TextGeneration structure.
     *
     * @param ptr    The pointer to the native Rust TextGeneration structure.
     * @param prompt The initial text or prompt based on which text is generated.
     * @param length The desired length of the generated text.
     * @return A String containing the generated text.
     */
    private native String generate_text(long ptr, String prompt, int length);

    /**
     * Generates text based on a given prompt and length.
     *
     * @param prompt The initial text or prompt based on which text is generated.
     * @param length The desired length of the generated text.
     * @return A String containing the generated text.
     */
    public String generateText(String prompt, int length) {
        return generate_text(ptr, prompt, length);
    }
}
