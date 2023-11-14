// This is the interface to the JVM that we'll
// call the majority of our methods on.
use jni::JNIEnv;
// These objects are what you should use as arguments to your native function.
// They carry extra lifetime information to prevent them escaping from the
// current local frame (which is the scope within which local (temporary)
// references to Java objects remain valid)
use jni::objects::{JClass, JString};
use jni::sys::{jlong, jstring};

use link_gpt::message::Message;

// JNIEXPORT jstring JNICALL Java_eu_iashchak_linkgpt_Message_to_1string
//   (JNIEnv *, jobject);
#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Message_to_1string(
    env: JNIEnv,
    _class: JClass,
    message_ptr: jlong,
) -> jstring {
    let message = &*(message_ptr as *mut Message);
    let output = env
        .new_string(message.to_string())
        .expect("Couldn't create java string!");

    // Finally, extract the raw pointer to return.
    output.into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Message_from_1string<'local>(
    mut env: JNIEnv,
    _class: JClass,
    input: JString<'local>,
) -> jlong {
    let input: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    let message = Message::from(input.to_string());

    Box::into_raw(Box::new(message)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Message_from_1tuple<'local>(
    mut env: JNIEnv,
    _class: JClass,
    from: JString<'local>,
    content: JString<'local>,
) -> jlong {
    let from: String = env
        .get_string(&from)
        .expect("Couldn't get java string!")
        .into();
    let content: String = env
        .get_string(&content)
        .expect("Couldn't get java string!")
        .into();

    let message = Message::from((from.as_str(), content.as_str()));

    Box::into_raw(Box::new(message)) as jlong
}

// This keeps Rust from "mangling" the name and making it unique for this
// crate.
#[no_mangle]
pub extern "system" fn Java_eu_iashchak_linkgpt_HelloWorld_hello<'local>(
    mut env: JNIEnv<'local>,
    // This is the class that owns our static method. It's not going to be used,
    // but still must be present to match the expected signature of a static
    // native method.
    _: JClass<'local>,
    input: JString<'local>,
) -> jstring {
    // First, we have to get the string out of Java. Check out the `strings`
    // module for more info on how this works.
    let input: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    // Then we have to create a new Java string to return. Again, more info
    // in the `strings` module.
    let output = env
        .new_string(format!("Hello, {}!", input))
        .expect("Couldn't create java string!");

    // Finally, extract the raw pointer to return.
    output.into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Dialog_to_1string<'local>(
    env: JNIEnv<'local>,
    _class: JClass<'local>,
    dialog_ptr: jlong,
) -> jstring {
    let dialog = &*(dialog_ptr as *mut link_gpt::dialog::Dialog);
    let output = env
        .new_string(dialog.to_string())
        .expect("Couldn't create java string!");

    // Finally, extract the raw pointer to return.
    output.into_raw()
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Dialog_with_1system<'local>(
    _: JNIEnv<'local>,
    _class: JClass<'local>,
    dialog_ptr: jlong,
) -> jlong {
    let dialog = &*(dialog_ptr as *mut link_gpt::dialog::Dialog);
    let dialog = dialog.with_system();

    Box::into_raw(Box::new(dialog)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Dialog_new_1dialog<'local>(
    _: JNIEnv<'local>,
    _class: JClass<'local>,
) -> jlong {
    let dialog = link_gpt::dialog::Dialog::new();

    Box::into_raw(Box::new(dialog)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_Dialog_add_1message<'local>(
    _: JNIEnv<'local>,
    _class: JClass<'local>,
    dialog_ptr: jlong,
    message_ptr: jlong,
) -> jlong {
    let dialog = &mut *(dialog_ptr as *mut link_gpt::dialog::Dialog);
    let message = &*(message_ptr as *mut Message);
    let message = dialog.add_message(message.clone());

    Box::into_raw(Box::new(message)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_TextGeneration_new_1text_1generation(
    mut env: JNIEnv,
    _class: JClass,
    model_path: JString,
) -> jlong {
    let model_path: String = env
        .get_string(&model_path)
        .expect("Couldn't get java string!")
        .into();

    let text_generation = link_gpt::text_generation::TextGeneration::new(model_path.as_str())
        .expect("Couldn't create text generation!");

    Box::into_raw(Box::new(text_generation)) as jlong
}

#[no_mangle]
pub unsafe extern "system" fn Java_eu_iashchak_linkgpt_TextGeneration_generate_1text(
    mut env: JNIEnv,
    _class: JClass,
    text_generation_ptr: jlong,
    input: JString,
) -> jstring {
    let text_generation =
        &mut *(text_generation_ptr as *mut link_gpt::text_generation::TextGeneration);
    let prompt: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    println!("Prompt: {}", prompt);
    let generated_text = text_generation
        .run(prompt.as_str(), 32)
        .expect("Couldn't generate text!");

    let output = env
        .new_string(generated_text)
        .expect("Couldn't create java string!");

    // Finally, extract the raw pointer to return.
    output.into_raw()
}
