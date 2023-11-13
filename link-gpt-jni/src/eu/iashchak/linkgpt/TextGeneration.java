package eu.iashchak.linkgpt;

/**
 * This is a bound class to rust's eu.iashchak.linkgpt.TextGeneration struct.
 */
class TextGeneration {
    /**
     * Native contructor for eu.iashchak.linkgpt.TextGeneration.
     */
    native long new_text_generation(String model_path);

    /**
     * Native method for generating text.
     */
    native String generate_text(long ptr, String prompt, int length);

    long ptr;

    static {
        System.loadLibrary("link_gpt_jni");
    }

    public TextGeneration(String model_path) {
        ptr = new_text_generation(model_path);
        System.out.println("TextGeneration created");
    }

    public String generateText(String prompt, int length) {
        return generate_text(ptr, prompt, length);
    }

    public static void main(String[] args) {
        TextGeneration textGeneration = new TextGeneration("iashchak/link-gpt-7b");
        System.out.println(textGeneration.generateText("Hello, world!", 10));
    }
}