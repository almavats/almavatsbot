package ru.almatel.vats.service.strategy;

import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.dao.AlmaBotDao;
import ru.almatel.vats.domain.UserStatusDto;

import java.util.List;

import static ru.almatel.vats.domain.MessageFormat.MARKDOWN;
import static ru.almatel.vats.domain.MessageFormat.NONE;
import static ru.almatel.vats.utils.BotUtils.getName;
import static ru.almatel.vats.utils.BotUtils.getSendMessage;

public class StatusMessageProcessorImpl implements MessageProcessor {
    private static final String COMMAND = "/status";
    private static final String EMPTY_STATUS_LIST = "Записи о пользователе %s в базе данных отсутствуют";

    private AlmaBotDao botDao;

    public StatusMessageProcessorImpl(AlmaBotDao botDao) {
        this.botDao = botDao;
    }

    @Override
    @Transactional
    public SendMessage process(Message message) {
        String chatId = message.getChat().getId() + "";
        List<UserStatusDto> userStatuses = botDao.readUserStatus(chatId);
        StringBuilder answerText = new StringBuilder();
        if (userStatuses.isEmpty()) {
            answerText.append(String.format(EMPTY_STATUS_LIST, getName(message)));
        } else {
            answerText.append("Текущий статус оповещений:\n\n");
            for (UserStatusDto status : userStatuses) {
                answerText.append(" *лицевой счет:* ")
                        .append(status.getDomain())
                        .append("\n")
                        .append(" *api key:* ")
                        .append(status.getApiKey())
                        .append("\n")
                        .append(" *статус:* ")
                        .append(status.getDescription())
                        .append("\n")
                        .append("-----------------------\n");
            }
        }
        return getSendMessage(message.getChat().getId() + "", answerText.toString(), MARKDOWN);
    }

    @Override
    public boolean matchCommand(String command) {
        return command != null && command.startsWith(COMMAND);
    }
}