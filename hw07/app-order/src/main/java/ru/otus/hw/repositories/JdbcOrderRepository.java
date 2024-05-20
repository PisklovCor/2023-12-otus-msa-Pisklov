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
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Order create(Order order) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", order.getLogin());
        params.addValue("account_invoice", order.getAccountInvoice());
        params.addValue("sum_order", order.getSumOrder());
        params.addValue("status", order.isStatus());
        jdbc.update("insert into order_table (login, account_invoice, sum_order, status)" +
                        " values (:login, :account_invoice, :sum_order, :status)"
                , params, keyHolder, new String[]{"id"});

        Order createOrder = new Order();
        createOrder.setId(keyHolder.getKeyAs(Long.class));
        createOrder.setLogin(order.getLogin());
        createOrder.setAccountInvoice(order.getAccountInvoice());
        createOrder.setSumOrder(order.getSumOrder());
        createOrder.setStatus(order.isStatus());
        return createOrder;
    }

    @Override
    public Optional<Order> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, login, account_invoice, sum_order, status" +
                        " from order_table where login = :login"
                , params, new UsersResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Order> findAll() {
        return jdbc.getJdbcOperations().query("select id, login, account_invoice, sum_order, status" +
                " from order_table", new OrderRowMapper());
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class UsersResultSetExtractor implements ResultSetExtractor<Order> {

        @Override
        public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Order order = new Order();

            while (resultSet.next()) {
                order.setId(resultSet.getLong("id"));
                order.setLogin(resultSet.getString("login"));
                order.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                order.setSumOrder(resultSet.getInt("sum_order"));
                order.setStatus(resultSet.getBoolean("status"));
            }

            return order;
        }
    }

    private static class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final String login = resultSet.getString("login");
            final UUID uuid = UUID.fromString(resultSet.getString("account_invoice"));
            final Integer sumOrder = resultSet.getInt("sum_order");
            final boolean status = resultSet.getBoolean("status");
            return new Order(id, login, uuid, sumOrder, status);
        }
    }
}
