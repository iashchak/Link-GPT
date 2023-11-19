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
 * This class serves as a Java binding to the Rust 'Dialog' struct.
 * It allows the creation and manipulation of dialog structures, interfacing with
 * native operations through JNI for handling dialog interactions.
 */
public class Dialog {
    // Load the native library containing the Rust Dialog implementation
    static {
        LinkGPTLoader.load();
    }

    // Pointer to the native Rust Dialog structure
    private long ptr;

    /**
     * Constructs a Dialog instance.
     * It initializes a new native Rust Dialog structure.
     */
    public Dialog() {
        ptr = new_dialog();
    }

    /**
     * Creates a new native Rust Dialog structure.
     *
     * @return A long value representing a pointer to the native Rust Dialog structure.
     */
    private native long new_dialog();

    /**
     * Adds a message to the dialog.
     * This method wraps the native functionality to add a message to the Rust Dialog structure.
     *
     * @param ptr         The pointer to the native Rust Dialog structure.
     * @param message_ptr The pointer to the native Rust Message structure.
     * @return A long value representing a pointer to the updated Rust Dialog structure.
     */
    private native long add_message(long ptr, long message_ptr);

    /**
     * Adds a message to the dialog with only text content.
     *
     * @param text The text content of the message.
     * @return The Dialog object, allowing for method chaining.
     */
    public Dialog addMessage(String text) {
        Message message = new Message(text);
        add_message(ptr, message.getPtr());
        return this;
    }

    /**
     * Adds a message to the dialog with a sender and text content.
     *
     * @param from The sender of the message.
     * @param text The text content of the message.
     * @return The Dialog object, allowing for method chaining.
     */
    public Dialog addMessage(String from, String text) {
        Message message = new Message(from, text);
        add_message(ptr, message.getPtr());
        return this;
    }

    /**
     * Converts the native Rust Dialog structure to a String.
     *
     * @param ptr The pointer to the native Rust Dialog structure.
     * @return A String representation of the Dialog.
     */
    private native String to_string(long ptr);

    /**
     * Returns a String representation of the Dialog.
     *
     * @return A String containing the dialog content.
     */
    @Override
    public String toString() {
        return to_string(ptr);
    }

    /**
     * Adds a system message to the dialog.
     * This method wraps the native functionality to modify the Rust Dialog structure.
     *
     * @param ptr The pointer to the native Rust Dialog structure.
     * @return A long value representing a pointer to the updated Rust Dialog structure.
     */
    private native long with_system(long ptr);

    /**
     * Adds a system message to the dialog.
     *
     * @return The Dialog object, allowing for method chaining.
     */
    public Dialog withSystem() {
        ptr = with_system(ptr);
        return this;
    }
}
