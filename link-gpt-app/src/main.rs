use anyhow::Result;

use link_gpt::dialog::Dialog;
use link_gpt::text_generation::TextGeneration;

fn main() -> Result<()> {
    let mut pipeline = TextGeneration::new("iashchak/link-gpt-7b")?;
    let mut dialog = Dialog::new().with_system();
    dialog
        .add_message(("user1887162351", "Привет! Как поживаешь?").into())
        .add_message(("user1457214229", "Та хуй знает").into())
        .add_message(("user1887162351", "Что про путина думаешь?").into());
    let result = pipeline.run(&dialog.to_string(), 64)?; // user1887162351 Это мне нужно
    println!("{}", result);
    let (user, message) = result.split_at(result.find(' ').unwrap());
    dialog.add_message((user, message).into());
    println!("{}", dialog.to_string());
    Ok(())
}