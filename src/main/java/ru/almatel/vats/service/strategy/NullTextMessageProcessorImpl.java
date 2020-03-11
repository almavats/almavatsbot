package ru.almatel.vats.service.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class NullTextMessageProcessorImpl implements MessageProcessor {
    @Override
    public SendMessage process(Message message) {
        StringBuilder text = new StringBuilder(prepareLeaveMessage(message));
        if (text.length() == 0) {
            text.append(prepareNewMembersMessage(message));
        }
        return new SendMessage()
                .setChatId(message.getChat().getId())
                .setText(text.toString());
    }

    private String prepareLeaveMessage(Message message) {

        User leftUser = message.getLeftChatMember();
        if (leftUser == null) {
            return "";
        }
        String userName = leftUser.getUserName() == null ?
                leftUser.getFirstName() + " " + leftUser.getLastName() :
                leftUser.getUserName();
        return "Пользователь " + userName + " не будет получать уведомления о пропущенных вызовах.";
    }

    private String prepareNewMembersMessage(Message message) {
        List<User> addedUsers = message.getNewChatMembers();
        if (addedUsers != null && addedUsers.size() > 0) {
            StringBuilder users = new StringBuilder();
            for (User user : addedUsers) {
                String userName = user.getUserName() == null ? user.getFirstName() + " " + user.getLastName() :
                        user.getUserName();
                users.append(userName)
                        .append(", ");
            }
            if (addedUsers.size() == 1) {
                users.insert(0, "Пользователю ");
            } else {
                users.insert(0, "Пользователям: ");
            }
            users.delete((users.length() - 2), users.length());
            users.append(" будут доступны оповещения о пропущенных вызовах и о статусе отправки оповещений.");
            return users.toString();
        }
        return "";
    }

    @Override
    public boolean matchCommand(String command) {
        return command == null;
    }
}