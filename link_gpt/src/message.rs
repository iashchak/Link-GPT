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

use std::fmt;

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
pub struct Message {
    pub from: String,
    pub content: String,
}

impl From<(&str, &str)> for Message {
    fn from((from, content): (&str, &str)) -> Self {
        Self {
            from: from.to_string(),
            content: content.to_string(),
        }
    }
}

impl From<String> for Message {
    fn from(content: String) -> Self {
        Self {
            from: "user".to_string(),
            content,
        }
    }
}

impl fmt::Display for Message {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "<|im_start|>{}\n{}<|im_end|>", self.from, self.content)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_creation() {
        let message = Message::from(("user1887162351", "Это мне нужно"));
        assert_eq!(
            message,
            Message {
                from: "user1887162351".to_string(),
                content: "Это мне нужно".to_string(),
            }
        );
    }

    #[test]
    fn test_creation_from_string() {
        let message = Message::from("Это мне нужно".to_string());
        assert_eq!(
            message,
            Message {
                from: "user".to_string(),
                content: "Это мне нужно".to_string(),
            }
        );
    }

    #[test]
    fn test_into_string() {
        let message = Message::from(("user1887162351", "Это мне нужно"));
        let formatted_message = message.to_string();
        assert_eq!(
            formatted_message,
            "<|im_start|>user1887162351\nЭто мне нужно<|im_end|>"
        );
    }
}
