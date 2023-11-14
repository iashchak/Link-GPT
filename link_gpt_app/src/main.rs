use std::io::Write;
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
        println!("{}", dialog.to_string());
        print!("Введите сообщение: ");
        std::io::stdout().flush().unwrap();
        let mut line = String::new();
        std::io::stdin().read_line(&mut line).expect("Failed to read line");
        if line.is_empty() {
            break;
        }
        dialog.add_message(("user1", line.trim()).into());
        let mut prompt = dialog.to_string();
        prompt.push_str("<|im_start|>user2\n");
        let result = pipeline.run(&prompt, 64)?;
        println!("{}", result);
        dialog.add_message(("user2", result.as_str()).into());
    }
    Ok(())
}
