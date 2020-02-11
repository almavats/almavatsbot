package ru.almatel.vats.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.domain.MessageFormat;
import ru.almatel.vats.domain.TelegramUser;

public class BotUtils {
    public static TelegramUser getTelegramUser(Message message, int apiPosition) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setApiKey(message.getText().substring(apiPosition).trim());
        telegramUser.setName(message.getFrom().getUserName());
        telegramUser.setChatId(message.getFrom().getId());
        return telegramUser;
    }

    public static SendMessage getSendMessage(String chatId, String text, MessageFormat format) {
        SendMessage result = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        switch (format) {
            case HTML:
                return result.enableHtml(true);
            case MARKDOWN:
                return result.enableMarkdown(true);
            default:
                return result;
        }
    }
}
