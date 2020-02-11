package ru.almatel.vats.service;

import org.springframework.transaction.annotation.Transactional;
import ru.almatel.vats.dao.BotPropertyDao;
import ru.almatel.vats.domain.BotPropertyHolder;
import ru.almatel.vats.domain.ProxyPropertyHolder;

@Transactional(readOnly = true)
public class BotPropertyServiceImpl implements BotPropertyService {
    private BotPropertyDao propertyDao;
    private Long botId;
    private BotPropertyHolder botProperty;
    private ProxyPropertyHolder proxyProperty;

    public BotPropertyServiceImpl(BotPropertyDao propertyDao, Long botId) {
        this.propertyDao = propertyDao;
        this.botId = botId;
    }

    @Override
    public BotPropertyHolder readBotProperty() {
        if (botProperty == null) {
            botProperty = propertyDao.readBotProperty(botId);
        }
        return botProperty;
    }

    @Override
    public ProxyPropertyHolder readProxyProperty() {
        if (proxyProperty != null ) {
            return proxyProperty;
        } else if (botProperty == null) {
            readBotProperty();
        }
        if (botProperty.getProxyId() != null) {
            proxyProperty = propertyDao.readProxyProperty(botProperty.getProxyId());
        }
        return proxyProperty;
    }

}
