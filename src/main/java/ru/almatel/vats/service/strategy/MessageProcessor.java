package ru.almatel.vats.service.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Process telegram message depending on command
 */
public interface MessageProcessor {
    SendMessage process(Message message);

    boolean matchCommand(String command);
}