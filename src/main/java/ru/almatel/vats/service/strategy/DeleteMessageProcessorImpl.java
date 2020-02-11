package ru.almatel.vats.service.strategy;

import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.dao.AlmaBotDao;
import ru.almatel.vats.domain.TelegramUser;

import static ru.almatel.vats.domain.MessageFormat.MARKDOWN;
import static ru.almatel.vats.utils.BotUtils.getSendMessage;
import static ru.almatel.vats.utils.BotUtils.getTelegramUser;

public class DeleteMessageProcessorImpl implements MessageProcessor {
    private static final String COMMAND = "/delete";
    private static final String ANSWER_SUCCESS = "Отправка Вам уведомлений для указанной ВАТС прекращена.\n" +
            "Для возобновления активируйте отправку уведомлений командой: " +
            "/activate *vats-api-key*";
    private static final String ANSWER_ERR_APY_NOT_SET = "Ошибка: не указан *vats-api-key*.\nПопробуйте еще раз.";
    private static final String ANSWER_ERR_APY_NOT_FOUND = "Ошибка: не корректный *vats-api-key*.\n" +
            "Уточните *vats-api-key* в [личном кабинете ВАТС](https://lkvats.cifra1.ru/login) и повторите попытку.";
    private static final String DEF_ANSWER = "Ошибка: не распознана введенная команда.\n Попробуйте еще раз.";

    private AlmaBotDao botDao;

    public DeleteMessageProcessorImpl(AlmaBotDao botDao) {
        this.botDao = botDao;
    }

    @Override
    @Transactional
    public SendMessage process(Message message) {
        TelegramUser user = getTelegramUser(message, COMMAND.length());
        String text;
        switch (botDao.deleteTelegramUser(user)) {
            case -1:
                text = ANSWER_ERR_APY_NOT_SET;
                break;
            case -2:
                text = ANSWER_ERR_APY_NOT_FOUND;
                break;
            case 0:
                text = ANSWER_SUCCESS;
                break;
            default:
                text = DEF_ANSWER;
        }
        return getSendMessage(user.getChatId(), text, MARKDOWN);
    }

    @Override
    public boolean matchCommand(String command) {
        return command.startsWith(COMMAND);
    }
}