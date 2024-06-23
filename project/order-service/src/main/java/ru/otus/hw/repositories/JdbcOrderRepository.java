package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Order createOrder(Order order) {
        var keyHolder = new GeneratedKeyHolder();

        final String status = "wait";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("account_id", order.getAccountId());
        params.addValue("title", order.getTitle());
        params.addValue("author", order.getAuthor());
        params.addValue("status", status);
        jdbc.update("insert into table_order (account_id, title, author, status)" +
                        " values (:account_id, :title, :author, :status)"
                , params, keyHolder, new String[]{"id"});

        order.setId(keyHolder.getKeyAs(Long.class));
        order.setStatus(status);
        return order;
    }

    @Override
    public List<Order> findAll() {
        return jdbc.getJdbcOperations()
                .query("select id, account_id, title, author, status from table_order", new OrderBookRowMapper());
    }

    @Override
    public List<Order> findOrderByAccountId(long accountId) {

        String sql = "select id, account_id, title, author, status from table_order where account_id =%d"
                .formatted(accountId);

        return jdbc.getJdbcOperations()
                .query(sql, new OrderBookRowMapper());
    }

    @Override
    public Optional<Order> findOrderByTitleAndAuthor(String title, String author) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("author", author);
        return Optional.ofNullable(jdbc.query(
                "select id, account_id, title, author, status from table_order" +
                        " where title = :title and author = :author"
                , params, new OrderResultSetExtractor())).filter(b -> b.getId() != 0);

    }

    private static class OrderBookRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final long accountId = resultSet.getLong("account_id");
            final String title = resultSet.getString("title");
            final String author = resultSet.getString("author");
            final String status = resultSet.getString("status");
            return new Order(id, accountId, title, author, status);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class OrderResultSetExtractor implements ResultSetExtractor<Order> {

        @Override
        public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Order order = new Order();

            while (resultSet.next()) {
                order.setId(resultSet.getLong("id"));
                order.setAccountId(resultSet.getInt("account_id"));
                order.setTitle(resultSet.getString("title"));
                order.setAuthor(resultSet.getString("author"));
                order.setStatus(resultSet.getString("status"));
            }

            return order;
        }
    }
}
