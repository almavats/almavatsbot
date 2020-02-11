package ru.almatel.vats.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.almatel.vats.domain.BotPropertyHolder;
import ru.almatel.vats.domain.ProxyPropertyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BotPropertyDaoJdbc implements BotPropertyDao {
    private static final String SELECT_BOT_PROPERTY = "SELECT srv_name, sender_pswd, proxy_id from send" +
            ".tb_sender_params where sender_id = ?";
    private static final String SELECT_PROXY_PROPERTY = "SELECT proxy_type, proxy_host, proxy_port, proxy_user, " +
            "proxy_pswd from send.tb_proxy sp where proxy_id = ?";

    private JdbcOperations jdbc;

    public BotPropertyDaoJdbc(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public BotPropertyHolder readBotProperty(Long botId) {
        return jdbc.queryForObject(SELECT_BOT_PROPERTY, new Object[]{botId}, new RowMapper<BotPropertyHolder>() {
            @Override
            public BotPropertyHolder mapRow(ResultSet resultSet, int i) throws SQLException {
                BotPropertyHolder holder = new BotPropertyHolder();
                holder.setName(resultSet.getString(1));
                holder.setToken(resultSet.getString(2));
                holder.setProxyId(resultSet.getLong(3));
                return holder;
            }
        });
    }

    @Override
    public ProxyPropertyHolder readProxyProperty(Long proxyId) {
        return jdbc.queryForObject(SELECT_PROXY_PROPERTY, new Object[]{proxyId}, new RowMapper<ProxyPropertyHolder>() {
            @Override
            public ProxyPropertyHolder mapRow(ResultSet resultSet, int i) throws SQLException {
                ProxyPropertyHolder holder = new ProxyPropertyHolder();
                holder.setType(resultSet.getString(1));
                holder.setHost(resultSet.getString(2));
                holder.setPort(resultSet.getInt(3));
                holder.setUser(resultSet.getString(4));
                holder.setPassword(resultSet.getString(5));
                return holder;
            }
        });
    }
}