package ru.almatel.vats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.domain.MessageFormat;
import ru.almatel.vats.service.strategy.MessageProcessor;

import java.util.Arrays;
import java.util.List;

import static ru.almatel.vats.utils.BotUtils.getSendMessage;

public class MessageProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorService.class);
    private static final String DEF_ANSWER = "Команда не распознана. Попробуйте еще раз.\nПоддерживаемые команды:\n" +
            "/start - начало работы с ботом.\n" +
            "/activate *vats-api-key* - активировать уведомления о пропущенных вызовах.\n" +
            "/mute *vats-api-key* - временно приостановить отправку уведомлений.\n" +
            "/status - получить текущий статус активности рассылки сообщений.\n" +
            "/help - просмотреть список доступных команд.";

    private List<MessageProcessor> processors;

    public MessageProcessorService(MessageProcessor... processors) {
        this.processors = Arrays.asList(processors);
    }

    public SendMessage process(Message message) {
        for (MessageProcessor processor : processors) {
            if (processor.matchCommand(message.getText())) {
                logger.info("PROCESSOR FOUND!");
                return processor.process(message);
            }
        }
        return getSendMessage(message.getChat().getId() + "", DEF_ANSWER, MessageFormat.MARKDOWN);
    }
}