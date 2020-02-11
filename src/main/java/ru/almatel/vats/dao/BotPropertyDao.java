package ru.almatel.vats.dao;

import ru.almatel.vats.domain.BotPropertyHolder;
import ru.almatel.vats.domain.ProxyPropertyHolder;

public interface BotPropertyDao {

    BotPropertyHolder readBotProperty(Long botId);

    ProxyPropertyHolder readProxyProperty(Long proxyId);
}
