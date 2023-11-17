package eu.iashchak.linkgpt;

import eu.iashchak.linkgpt.utils.NativeLibrary;

/**
 * This class serves as a Java binding to the Rust 'TextGeneration' struct,
 * facilitating text generation using a specified model.
 */
public class TextGeneration {
    // Load the native library containing the Rust Text Generation implementation
    static {
        try {
            System.load(NativeLibrary.getLibraryPath());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw new RuntimeException(e);
        }
    }

    // Pointer to the native Rust TextGeneration structure
    private final long ptr;

    /**
     * Constructor that initializes the TextGeneration instance.
     *
     * @param model_path The path to the text generation model. This path is
     *                   used by the native Rust code to initialize the model.
     */
    public TextGeneration(String model_path) {
        ptr = new_text_generation(model_path);
    }

    /**
     * Native method that constructs a new TextGeneration instance in Rust.
     * This method is declared native as it interacts with Rust code.
     *
     * @param model_path The path to the model used for text generation.
     * @return A long value representing a pointer to the native Rust TextGeneration structure.
     */
    private native long new_text_generation(String model_path);

    /**
     * Native method to generate text using the Rust TextGeneration structure.
     *
     * @param ptr    The pointer to the native Rust TextGeneration structure.
     * @param prompt The initial text or prompt based on which text is generated.
     * @param length The desired length of the generated text.
     * @return A String containing the generated text.
     */
    private native String generate_text(long ptr, String prompt, int length);

    /**
     * Generates text based on a given prompt and length.
     *
     * @param prompt The initial text or prompt based on which text is generated.
     * @param length The desired length of the generated text.
     * @return A String containing the generated text.
     */
    public String generateText(String prompt, int length) {
        return generate_text(ptr, prompt, length);
    }
}
