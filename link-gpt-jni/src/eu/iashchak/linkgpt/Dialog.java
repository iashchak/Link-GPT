package eu.iashchak.linkgpt;


/**
 * This is a bound class to rust's eu.iashchak.linkgpt.Dialog struct.
 */
public class Dialog {
    long ptr;


    native String to_string(long ptr);

    public String toString() {
        return to_string(ptr);
    }

    native long with_system(long ptr);

    native long new_dialog();

    public Dialog withSystem() {
        ptr = with_system(ptr);
        return this;
    }

    public Dialog() {
        ptr = new_dialog();
    }

    native long add_message(long ptr, long message_ptr);

    public Dialog addMessage(String text) {
        Message message = new Message(text);
        add_message(ptr, message.ptr);
        return this;
    }

    public Dialog addMessage(String from, String text) {
        Message message = new Message(from, text);
        add_message(ptr, message.ptr);
        return this;
    }

    static {
        System.loadLibrary("link_gpt_jni");
    }

    public static void main(String[] args) {
        Dialog dialog = new Dialog()
                .withSystem()
                .addMessage("Hello, world!")
                .addMessage("Josh", "Hello, world!");

        System.out.println(dialog);
    }
}
