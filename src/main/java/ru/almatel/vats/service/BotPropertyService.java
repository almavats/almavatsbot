package ru.almatel.vats.service;

import ru.almatel.vats.domain.BotPropertyHolder;
import ru.almatel.vats.domain.ProxyPropertyHolder;

public interface BotPropertyService {
    BotPropertyHolder readBotProperty();

    ProxyPropertyHolder readProxyProperty();
}