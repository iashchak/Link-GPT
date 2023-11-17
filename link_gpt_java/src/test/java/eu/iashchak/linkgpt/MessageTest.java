package eu.iashchak.linkgpt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @Test
    public void testMessageFromString() {
        Message message = new Message("Hello, world!");

        assertEquals(message.toString(), "<|im_start|>user\nHello, world!<|im_end|>");
    }

    @Test
    public void testMessageFromTuple() {
        Message message = new Message("Josh", "Hello, world!");

        assertEquals(message.toString(), "<|im_start|>Josh\nHello, world!<|im_end|>");
    }
}