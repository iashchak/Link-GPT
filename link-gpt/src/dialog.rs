use crate::message::Message;

pub struct Dialog {
    pub messages: Vec<Message>,
}

impl Dialog {
    pub fn new() -> Self {
        Self {
            messages: Vec::new(),
        }
    }

    pub fn with_system(&self) -> Self {
        let system_message_text = "Ты ИИ обученный на беседах с людьми в чате Игоря Линка. Твоя задача - продолжить диалог.";
        let system_message_role = "system";
        let mut messages = vec![Message::from((system_message_role, system_message_text))];
        messages.append(&mut self.messages.clone());
        Self {
            messages,
        }
    }

    pub fn add_message(&mut self, message: Message) -> &mut Dialog {
        self.messages.push(message);
        return self;
    }
}

impl ToString for Dialog {
    fn to_string(&self) -> String {
        return self.messages.iter().map(|message| message.to_string()).collect::<Vec<String>>().join("\n");
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_creation() {
        let mut dialog = Dialog::new().with_system();
        dialog.add_message(("user1887162351", "Это мне нужно").into());
        dialog.add_message(("user1457214229", "Тебя у келя забанили ?").into());
        dialog.add_message(("user1887162351", "Походу").into());
        assert_eq!(dialog.to_string(), "<|im_start|>system\nТы ИИ обученный на беседах с людьми в чате Игоря Линка. Твоя задача - продолжить диалог.<|im_end|>\n<|im_start|>user1887162351\nЭто мне нужно<|im_end|>\n<|im_start|>user1457214229\nТебя у келя забанили ?<|im_end|>\n<|im_start|>user1887162351\nПоходу<|im_end|>");
    }

}