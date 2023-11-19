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

package eu.iashchak.crossutils;

public enum Triplet {
    LINUX_X86_64("x86_64-unknown-linux-gnu", ".so", "lib"), WINDOWS_X86_64("x86_64-pc-windows-gnu", ".dll", ""), MACOS_AARCH64("aarch64-apple-darwin", ".dylib", "lib"), ANDROID_ARMV7("armv7-linux-androideabi", ".so", "lib");

    private final String name;
    private final String libExtension;
    private final String libPrefix;

    Triplet(String name, String libExtension, String libPrefix) {
        this.name = name;
        this.libExtension = libExtension;
        this.libPrefix = libPrefix;
    }

    public String getName() {
        return name;
    }

    public String getLibFileName(String libName) {
        return libPrefix + libName + libExtension;
    }
}
