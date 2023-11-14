package eu.iashchak.linkgpt;

/**
 * This class serves as a Java binding to the Rust 'Message' struct.
 * It provides functionality for creating and manipulating message objects,
 * with support for native operations through JNI.
 */
public class Message {
    // Pointer to the native Rust Message structure
    private final long ptr;

    // Getter for the pointer to the native Rust Message structure to be used in Dialog.java
    public long getPtr() {
        return ptr;
    }

    // Load the native library containing the Rust Message implementation
    static {
        try {
            System.loadLibrary("resource/libs/link_gpt/x86_64-pc-windows-gnu/link_gpt_jni");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        } catch (SecurityException e) {
            System.err.println("Security exception.\n" + e);
            System.exit(1);
        }
    }

    /**
     * Constructs a Message instance from a single string.
     * It initializes the native Rust structure for the message.
     *
     * @param content The content of the message.
     */
    public Message(String content) {
        ptr = from_string(content);
    }

    /**
     * Constructs a Message instance from a tuple of strings.
     * It initializes the native Rust structure with a sender and content.
     *
     * @param from    The sender of the message.
     * @param content The content of the message.
     */
    public Message(String from, String content) {
        ptr = from_tuple(from, content);
    }

    /**
     * Converts the native Rust Message structure to a String.
     *
     * @param ptr The pointer to the native Rust Message structure.
     * @return A String representation of the Message.
     */
    private native String to_string(long ptr);

    /**
     * Creates a native Rust Message structure from a single string.
     *
     * @param content The content of the message.
     * @return A long value representing a pointer to the native Rust Message structure.
     */
    private native long from_string(String content);

    /**
     * Creates a native Rust Message structure from a tuple of strings.
     *
     * @param from    The sender of the message.
     * @param content The content of the message.
     * @return A long value representing a pointer to the native Rust Message structure.
     */
    private native long from_tuple(String from, String content);

    /**
     * Returns a String representation of the Message.
     *
     * @return A String containing the message content.
     */
    @Override
    public String toString() {
        return to_string(ptr);
    }
}
