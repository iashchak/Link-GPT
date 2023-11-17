package eu.iashchak.linkgpt;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DialogTest {
    @Test
    public void testDialog() {
        Dialog dialog = new Dialog().addMessage("Hello, world!").addMessage("Josh", "Hello, world!");

        assertEquals(dialog.toString(), "<|im_start|>user\nHello, world!<|im_end|>\n<|im_start|>Josh\nHello, world!<|im_end|>");
    }

    @Test
    public void testShouldAddSystemMessage() {
        Dialog dialog = new Dialog().withSystem().addMessage("Hello, world!").addMessage("Josh", "Hello, world!");

        assertEquals(dialog.toString(), "<|im_start|>system\nТы ИИ обученный на беседах с людьми в чате Игоря Линка. Твоя задача - продолжить диалог.<|im_end|>\n<|im_start|>user\nHello, world!<|im_end|>\n<|im_start|>Josh\nHello, world!<|im_end|>");
    }
}