package ru.almatel.vats.service.strategy;

import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.dao.AlmaBotDao;
import ru.almatel.vats.domain.TelegramUser;

import static ru.almatel.vats.domain.MessageFormat.MARKDOWN;
import static ru.almatel.vats.utils.BotUtils.getSendMessage;
import static ru.almatel.vats.utils.BotUtils.getTelegramUser;

public class ActivateMessageProcessorImpl implements MessageProcessor {
    private static final String COMMAND = "/activate";
    private static final String ANSWER_SUCCESS = "отправка уведомлений активирована.";
    private static final String ANSWER_BAN_BY_ADMIN = "*Теукущий статус:* Отключено администратором.\n" +
            "Для восстановления доступа обратитесь к администратору Вашей ВАТС.";
    private static final String ANSWER_ERR_APY_NOT_SET = "Ошибка активации: не указан *vats-api-key*.\n" +
            "Попробуйте еще раз.";
    private static final String ANSWER_ERR_APY_NOT_FOUND = "Ошибка активации: не корректный *vats-api-key* .\n" +
            "Уточните *vats-api-key* в [личном кабинете ВАТС](https://lkvats.cifra1.ru/login) и повторите " +
            "попытку активации.";
    private static final String DEF_ANSWER = "Ошибка активации: не распознана введенная команда.\n Попробуйте еще раз.";
    private static final String DESCRIPTION = "Уведомления подключены";

    private AlmaBotDao botDao;

    public ActivateMessageProcessorImpl(AlmaBotDao botDao) {
        this.botDao = botDao;
    }

    @Override
    @Transactional
    public SendMessage process(Message message) {
        TelegramUser user = getTelegramUser(message, COMMAND.length());
        user.setState(1);
        user.setDescription(DESCRIPTION);
        String text;
        switch (botDao.saveTelegramUser(user)) {
            case -1:
                text =ANSWER_ERR_APY_NOT_SET;
                break;
            case -2:
                text = ANSWER_ERR_APY_NOT_FOUND;
                break;
            case -3:
                text = ANSWER_BAN_BY_ADMIN;
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