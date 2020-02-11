package ru.almatel.vats.service.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class HelpMessageProcessorImpl implements MessageProcessor {
    private static final String COMMAND = "/help";
    private static final String ANSWER_TEXT = "Cписок поддерживаемых команд:\n" +
            "/start - начало работы с ботом.\n" +
            "/activate *vats-api-key* - активировать уведомления о пропущенных вызовах.\n" +
            "/mute *vats-api-key* - временно приостановить отправку уведомлений.\n" +
            "/status : получить текущий статус активности рассылки сообщений.\n" +
            "/help : просмотреть список доступных команд.\n\n" +
            "Для некоторых команд требуется ввести *vats-api-key*. Вы можете его получить в [личном кабинете Вашей ВАТС]" +
            "(https://lkvats.cifra1.ru/login).";

    @Override
    public SendMessage process(Message message) {
        return new SendMessage()
                .setChatId(message.getFrom().getId() + "")
                .setText(ANSWER_TEXT)
                .enableMarkdown(true);
    }

    @Override
    public boolean matchCommand(String command) {
        return command.startsWith(COMMAND);
    }
}