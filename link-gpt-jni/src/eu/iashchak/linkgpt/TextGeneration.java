package eu.iashchak.linkgpt;

/**
 * This is a bound class to rust's eu.iashchak.linkgpt.TextGeneration struct.
 */
class TextGeneration {
    /**
     * Native contructor for eu.iashchak.linkgpt.TextGeneration.
     */
    native TextGeneration new_text_generation(String model_path);

    /**
     * Native method for generating text.
     */
    native String generate_text(String prompt, int length);

    static {
        System.loadLibrary("link-gpt-jni");
    }
}