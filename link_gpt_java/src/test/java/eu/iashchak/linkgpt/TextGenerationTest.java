package eu.iashchak.linkgpt;

import org.junit.jupiter.api.Test;

public class TextGenerationTest {
    @Test
    public void testTextGeneration() {
        String prompt = "- Hello Mike!\n-What's up?";

        TextGeneration textGeneration = new TextGeneration("iashchak/link-gpt-7b");

        String result = textGeneration.generateText(prompt, 10);

        assert !result.isEmpty();
    }
}
