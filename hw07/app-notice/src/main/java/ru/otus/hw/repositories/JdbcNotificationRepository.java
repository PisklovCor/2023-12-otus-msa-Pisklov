package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcNotificationRepository implements NotificationRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Notification create(Notification notification) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", notification.getLogin());
        params.addValue("account_invoice", notification.getAccountInvoice());
        params.addValue("sum_order", notification.getSumOrder());
        params.addValue("message", notification.getMessage());
        jdbc.update("insert into notifications (login, account_invoice, sum_order, message)" +
                        " values (:login, :account_invoice, :sum_order, :message)"
                , params, keyHolder, new String[]{"id"});

        Notification createNotification = new Notification();
        createNotification.setId(keyHolder.getKeyAs(Long.class));
        createNotification.setLogin(notification.getLogin());
        createNotification.setAccountInvoice(notification.getAccountInvoice());
        createNotification.setSumOrder(notification.getSumOrder());
        createNotification.setMessage(notification.getMessage());
        return createNotification;
    }

    @Override
    public List<Notification> findAll() {
        return jdbc.getJdbcOperations().query("select id, login, account_invoice, sum_order, message" +
                " from notifications", new NotificationRowMapper());
    }

    @Override
    public Optional<Notification> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, login, account_invoice, sum_order, message" +
                        " from notifications where login = :login"
                , params, new NotificationResultSetExtractor())).filter(b -> b.getId() != 0);
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class NotificationResultSetExtractor implements ResultSetExtractor<Notification> {

        @Override
        public Notification extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Notification notification = new Notification();

            while (resultSet.next()) {
                notification.setId(resultSet.getLong("id"));
                notification.setLogin(resultSet.getString("login"));
                notification.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                notification.setSumOrder(resultSet.getInt("sum_order"));
                notification.setMessage(resultSet.getString("message"));
            }

            return notification;
        }
    }

    private static class NotificationRowMapper implements RowMapper<Notification> {

        @Override
        public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final String login = resultSet.getString("login");
            final UUID uuid = UUID.fromString(resultSet.getString("account_invoice"));
            final Integer sumOrder = resultSet.getInt("sum_order");
            final String message = resultSet.getString("message");
            return new Notification(id, login, uuid, sumOrder, message);
        }
    }
}
