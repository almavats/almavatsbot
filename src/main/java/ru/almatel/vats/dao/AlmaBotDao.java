package ru.almatel.vats.dao;

import ru.almatel.vats.domain.TelegramUser;
import ru.almatel.vats.domain.UserStatusDto;

import java.util.List;

public interface AlmaBotDao {
    Integer saveTelegramUser(TelegramUser user);

    Integer deleteTelegramUser(TelegramUser user);

    List<UserStatusDto> readUserStatus(String chatId);
}