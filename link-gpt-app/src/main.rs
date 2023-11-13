use anyhow::Result;

use link_gpt::dialog::Dialog;
use link_gpt::text_generation::TextGeneration;

fn main() -> Result<()> {
    let mut pipeline = TextGeneration::new("iashchak/link-gpt-7b")?;
    let mut dialog = Dialog::new().with_system();
    dialog
        .add_message(("user1", "Привет! Как поживаешь?").into())
        .add_message(("user2", "Та хуй знает").into())
        .add_message(("user1", "Что про путина думаешь?").into())
        .add_message(("user2", "Пиздец").into());
    loop {
        let mut line = String::new();
        std::io::stdin().read_line(&mut line).expect("Failed to read line");
        if line.is_empty() {
            break;
        }
        dialog.add_message(("user1", line.trim()).into());
        let result = pipeline.run(&dialog.to_string(), 64)?; // user1887162351 Это мне нужно
        let (user, message) = result.split_at(result.find(' ').unwrap());
        dialog.add_message((user, message).into());
    }
    Ok(())
}
