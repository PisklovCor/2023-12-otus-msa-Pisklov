package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNotificationRepository implements NotificationRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Notification createOrder(Notification notification) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("account_id", notification.getAccountId());
        params.addValue("email", notification.getEmail());
        params.addValue("message", notification.getMessage());
        params.addValue("title", notification.getTitle());
        params.addValue("author", notification.getAuthor());
        jdbc.update("insert into notification (account_id, email, message, title, author)" +
                        " values (:account_id, :email, :message, :title, :author)"
                , params, keyHolder, new String[]{"id"});

        notification.setId(keyHolder.getKeyAs(Long.class));
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        return jdbc.getJdbcOperations()
                .query("select id, account_id, email, message, title, author from notification",
                        new NotificationBookRowMapper());
    }

    @Override
    public List<Notification> findNotificationByAccountId(long accountId) {

        String sql = "select id, account_id, email, message, title, author from notification where account_id =%d"
                .formatted(accountId);

        return jdbc.getJdbcOperations()
                .query(sql, new NotificationBookRowMapper());
    }

    private static class NotificationBookRowMapper implements RowMapper<Notification> {

        @Override
        public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final long accountId = resultSet.getLong("account_id");
            final String email = resultSet.getString("email");
            final String message = resultSet.getString("message");
            final String title = resultSet.getString("title");
            final String author = resultSet.getString("author");
            return new Notification(id, accountId, email, message, title, author);
        }
    }
}
