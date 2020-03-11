package ru.almatel.vats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.almatel.vats.dao.AlmaBotDao;
import ru.almatel.vats.dao.AlmaBotDaoJdbc;
import ru.almatel.vats.dao.BotPropertyDao;
import ru.almatel.vats.dao.BotPropertyDaoJdbc;
import ru.almatel.vats.service.BotPropertyService;
import ru.almatel.vats.service.BotPropertyServiceImpl;
import ru.almatel.vats.service.strategy.*;
import ru.almatel.vats.service.MessageProcessorService;

import javax.sql.DataSource;

@Configuration
@PropertySource("file:app.properties")
public class AlmaBotConfig {
    @Value("${jdbc.driver}")
    private String driverName;
    @Value("${jdbc.url}")
    private String dbUrl;
    @Value("${jdbc.dbuser}")
    private String dbUser;
    @Value("${jdbc.dbpassword}")
    private String dbPassword;
    @Value("${bot.id}")
    private Long botId;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public BotPropertyDao botPropertyDao() {
        return new BotPropertyDaoJdbc(dataSource());
    }

    @Bean
    public AlmaBotDao almaBotDao() {
        return new AlmaBotDaoJdbc(dataSource());
    }

    @Bean
    public MessageProcessor startProcessor() {
        return new StartMessageProcessorImpl();
    }

    @Bean
    public MessageProcessor activateProcessor() {
        return new ActivateMessageProcessorImpl(almaBotDao());
    }

    @Bean
    public MessageProcessor muteProcessor() {
        return new MuteMessageProcessorImpl(almaBotDao());
    }

    @Bean
    public MessageProcessor statusProcessor() {
        return new StatusMessageProcessorImpl(almaBotDao());
    }

    @Bean
    public MessageProcessor helpProcessor() {
        return new HelpMessageProcessorImpl();
    }

    @Bean
    public MessageProcessor nullTextProcessor() {
        return new NullTextMessageProcessorImpl();
    }

    @Bean
    public MessageProcessor deleteProcessor() {
        return new DeleteMessageProcessorImpl(almaBotDao());
    }

    @Bean
    public BotPropertyService botPropertyService() {
        return new BotPropertyServiceImpl(botPropertyDao(), botId);
    }

    @Bean
    public MessageProcessorService messageProcessorContext() {
        return new MessageProcessorService(startProcessor(), activateProcessor(), muteProcessor(), statusProcessor(),
                helpProcessor(), nullTextProcessor());
    }
}