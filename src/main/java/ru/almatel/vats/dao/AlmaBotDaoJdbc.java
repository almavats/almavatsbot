package ru.almatel.vats.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.almatel.vats.domain.TelegramUser;
import ru.almatel.vats.domain.UserStatusDto;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlmaBotDaoJdbc implements AlmaBotDao {
    private static final String SAVE_TELEGRAM_USER = "SELECT * FROM vats.int_update_telegram_users(?, ?, ?, ?, ?)";
    private static final String READ_USER_STATUS = "select * from vats.int_get_telegram_users_by_api_key(?)";
    private static final String DELETE_TELEGRAM_USER = "select * from vats.int_delete_telegram_user_by_api_key(?, ?)";

    private JdbcTemplate jdbc;

    public AlmaBotDaoJdbc(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Integer saveTelegramUser(TelegramUser user) {
        return jdbc.queryForObject(SAVE_TELEGRAM_USER,
                new Object[]{user.getApiKey(), user.getName(), user.getChatId(), user.getState(), user.getDescription()},
                Integer.class);
    }

    @Override
    public List<UserStatusDto> readUserStatus(String chatId) {
        return jdbc.query(READ_USER_STATUS, new Object[]{chatId}, new ResultSetExtractor<List<UserStatusDto>>() {
                    @Override
                    public List<UserStatusDto> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        List<UserStatusDto> userStatuses = new ArrayList<>();
                        while (resultSet.next()) {
                            UserStatusDto status = new UserStatusDto();
                            status.setChatId(resultSet.getString(1));
                            status.setName(resultSet.getString(2));
                            status.setCreateDt(resultSet.getDate(3));
                            status.setState(resultSet.getInt(4));
                            status.setStateDt(resultSet.getDate(5));
                            status.setDescription(resultSet.getString(6));
                            status.setApiKey(resultSet.getString(7));
                            status.setDomain(resultSet.getString(8));
                            userStatuses.add(status);
                        }
                        return userStatuses;
                    }
                }
        );
    }

    @Override
    public Integer deleteTelegramUser(TelegramUser user) {
        return jdbc.queryForObject(DELETE_TELEGRAM_USER, Integer.class, user.getApiKey(), user.getChatId());
    }
}
