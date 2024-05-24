package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.dto.OrderStatus;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

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
        params.addValue("description_order", order.getDescriptionOrder());
        params.addValue("sum_order", order.getSumOrder());
        params.addValue("status", order.getStatus());
        jdbc.update("insert into order_table (login, account_invoice, description_order, sum_order, status)" +
                        " values (:login, :account_invoice, :description_order, :sum_order, :status)"
                , params, keyHolder, new String[]{"id"});


        var optionalOrder = findById(keyHolder.getKeyAs(Long.class));

        return optionalOrder.orElseThrow(() -> new EntityNotFoundException("Order not created"));
    }

    @Override
    public Optional<Order> findByUUID(UUID uuid) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_invoice", uuid);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, login, account_invoice, description_order, sum_order, status" +
                        " from order_table where account_invoice = :account_invoice"
                , params, new OrderResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public Optional<Order> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, login, account_invoice, description_order, sum_order, status" +
                        " from order_table where login = :login"
                , params, new OrderResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Order> findAll() {
        return jdbc.getJdbcOperations().query(
                "select id, created_at, login, account_invoice, description_order, sum_order, status" +
                " from order_table", new OrderRowMapper());
    }

    @Override
    public void updateOrderStatus(long id, OrderStatus status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("status", status.toString());
        int result = jdbc.update("update order_table set status = :status  where id = :id", params);

        if (result < 1) {
            throw new EntityNotFoundException("The Order is not updated");
        }
    }

    private Optional<Order> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, login, account_invoice, description_order, sum_order, status" +
                        " from order_table where id = :id"
                , params, new OrderResultSetExtractor()));
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class OrderResultSetExtractor implements ResultSetExtractor<Order> {

        @Override
        public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Order order = new Order();

            while (resultSet.next()) {
                order.setId(resultSet.getLong("id"));
                order.setCreatedAt(resultSet.getTimestamp("created_at"));
                order.setLogin(resultSet.getString("login"));
                order.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                order.setDescriptionOrder(resultSet.getString("description_order"));
                order.setSumOrder(resultSet.getInt("sum_order"));
                order.setStatus(resultSet.getString("status"));
            }

            return order;
        }
    }

    private static class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final Date createdAt = resultSet.getTimestamp("created_at");
            final String login = resultSet.getString("login");
            final UUID accountInvoice = UUID.fromString(resultSet.getString("account_invoice"));
            final String descriptionOrder = resultSet.getString("description_order");
            final Integer sumOrder = resultSet.getInt("sum_order");
            final String status = resultSet.getString("status");
            return new Order(id, createdAt, login, accountInvoice, descriptionOrder, sumOrder, status);
        }
    }
}
