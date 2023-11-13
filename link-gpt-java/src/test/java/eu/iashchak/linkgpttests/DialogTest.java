package eu.iashchak.linkgpttests;


import eu.iashchak.linkgpt.Dialog;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DialogTest {
    @Test
    public void testDialog() {
        Dialog dialog = new Dialog()
                .withSystem()
                .addMessage("Hello, world!")
                .addMessage("Josh", "Hello, world!");

        assertEquals(dialog.toString(), "<|im_start|>system\nHello, world!<|im_end|>\n<|im_start|>Josh\nHello, world!<|im_end|>");
    }
}