package eu.iashchak.linkgpttests;

import eu.iashchak.linkgpt.Message;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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