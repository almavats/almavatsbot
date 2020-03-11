package ru.almatel.vats.almabot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.almatel.vats.domain.BotPropertyHolder;
import ru.almatel.vats.service.MessageProcessorService;

public class AlmaVatsBot extends TelegramLongPollingBot {
    private BotPropertyHolder botProperty;
    private MessageProcessorService messageProcessor;

    public AlmaVatsBot(BotPropertyHolder botProperty, DefaultBotOptions options, MessageProcessorService processorContext) {
        super(options);
        this.botProperty = botProperty;
        this.messageProcessor = processorContext;
    }

    @Override
    public String getBotToken() {
        return botProperty.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                execute(messageProcessor.process(update.getMessage()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botProperty.getName();
    }
}