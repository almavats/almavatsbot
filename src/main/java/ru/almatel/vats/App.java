package ru.almatel.vats;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.almatel.vats.almabot.AlmaVatsBot;
import ru.almatel.vats.domain.ProxyPropertyHolder;
import ru.almatel.vats.service.BotPropertyService;
import ru.almatel.vats.service.MessageProcessorService;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        MessageProcessorService messageProcessor = ctx.getBean(MessageProcessorService.class);
        BotPropertyService botPropertyService = ctx.getBean(BotPropertyService.class);
        final ProxyPropertyHolder proxyPropertyHolder = botPropertyService.readProxyProperty();

        if (proxyPropertyHolder != null && proxyPropertyHolder.getUser() != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyPropertyHolder.getUser(),
                            proxyPropertyHolder.getPassword().toCharArray());
                }
            });
        }
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        if (proxyPropertyHolder != null) {
            DefaultBotOptions options = new DefaultBotOptions();
            options.setProxyHost(proxyPropertyHolder.getHost());
            options.setProxyPort(proxyPropertyHolder.getPort());

            switch (proxyPropertyHolder.getType()) {
                case "SOCKS5":
                    options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
                    break;
                default:
                    options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }
            botsApi.registerBot(new AlmaVatsBot(botPropertyService.readBotProperty(), options, messageProcessor));
        }
    }
}