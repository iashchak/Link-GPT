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
 * This class serves as a Java binding to the Rust 'Message' struct.
 * It provides functionality for creating and manipulating message objects,
 * with support for native operations through JNI.
 */
public class Message {
    // Load the native library containing the Rust Message implementation
    static {
        LinkGPTLoader.load();
    }

    // Pointer to the native Rust Message structure
    private final long ptr;

    /**
     * Constructs a Message instance from a single string.
     * It initializes the native Rust structure for the message.
     *
     * @param content The content of the message.
     */
    public Message(String content) {
        ptr = from_string(content);
    }

    /**
     * Constructs a Message instance from a tuple of strings.
     * It initializes the native Rust structure with a sender and content.
     *
     * @param from    The sender of the message.
     * @param content The content of the message.
     */
    public Message(String from, String content) {
        ptr = from_tuple(from, content);
    }

    // Getter for the pointer to the native Rust Message structure to be used in Dialog.java
    public long getPtr() {
        return ptr;
    }

    /**
     * Converts the native Rust Message structure to a String.
     *
     * @param ptr The pointer to the native Rust Message structure.
     * @return A String representation of the Message.
     */
    private native String to_string(long ptr);

    /**
     * Creates a native Rust Message structure from a single string.
     *
     * @param content The content of the message.
     * @return A long value representing a pointer to the native Rust Message structure.
     */
    private native long from_string(String content);

    /**
     * Creates a native Rust Message structure from a tuple of strings.
     *
     * @param from    The sender of the message.
     * @param content The content of the message.
     * @return A long value representing a pointer to the native Rust Message structure.
     */
    private native long from_tuple(String from, String content);

    /**
     * Returns a String representation of the Message.
     *
     * @return A String containing the message content.
     */
    @Override
    public String toString() {
        return to_string(ptr);
    }
}
