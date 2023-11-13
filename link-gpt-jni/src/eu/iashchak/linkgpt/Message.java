package eu.iashchak.linkgpt;

/**
 * This is a bound class to rust's eu.iashchak.linkgpt.Message struct.
 */
public class Message {
    native String to_string(long ptr);

    public String toString() {
        return to_string(ptr);
    }

    native long from_string(String content);

    native long from_tuple(String from, String content);

    final long ptr;

    static {
        try {
            System.loadLibrary("link_gpt_jni");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        } catch (SecurityException e) {
            System.err.println("Security exception.\n" + e);
            System.exit(1);
        }
    }

    public Message(String content) {
        ptr = from_string(content);
    }

    public Message(String from, String content) {
        ptr = from_tuple(from, content);
    }

    public static void main(String[] args) {
        Message message = new Message("Hello, world!");
        System.out.println(message);

        Message message2 = new Message("Josh", "Hello, world!");
        System.out.println(message2);
    }
}

