package ru.almatel.vats.service.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.almatel.vats.dao.AlmaBotDao;
import ru.almatel.vats.domain.TelegramUser;

import static ru.almatel.vats.domain.MessageFormat.*;
import static ru.almatel.vats.utils.BotUtils.getSendMessage;
import static ru.almatel.vats.utils.BotUtils.getTelegramUser;

public class MuteMessageProcessorImpl implements MessageProcessor {
    private static final String COMMAND = "/mute";
    private static final int STATE = -1;
    private static final String DESCRIPTION = "Отключено пользователем";
    private static final String ANSWER_ERR_APY_NOT_FOUND = "Ошибка: не корректный *vats-api-key*.\n" +
            "Уточните APY KEY в [личном кабинете ВАТС](https://lkvats.cifra1.ru/login) и повторите попытку активации.";
    private static final String ANSWER_SUCCESS = "отправка уведомлений *отключена*.";
    private static final String ANSWER_BAN_BY_ADMIN = "*Текущий статус:* Отключено администратором.\n" +
            "Управление статусом для Вас *не доступно*.\n" +
            "Для восстановления доступа обратитесь к администратору Вашей ВАТС.";
    private static final String DEF_ANSWER = "Ошибка: не распознана введенная команда.\n Попробуйте еще раз.";

    private AlmaBotDao botDao;

    public MuteMessageProcessorImpl(AlmaBotDao botDao) {
        this.botDao = botDao;
    }

    @Override
    public SendMessage process(Message message) {
        TelegramUser user = getTelegramUser(message, COMMAND.length());
        user.setState(STATE);
        user.setDescription(DESCRIPTION);
        String text;
        switch (botDao.saveTelegramUser(user)) {
            case -1:
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
