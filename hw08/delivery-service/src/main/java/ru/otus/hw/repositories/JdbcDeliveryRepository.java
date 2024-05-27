package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.dto.Status;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Delivery;

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
public class JdbcDeliveryRepository implements DeliveryRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Delivery create(Delivery delivery) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("store_id", delivery.getStoreId());
        params.addValue("payment_id", delivery.getPaymentId());
        params.addValue("order_id", delivery.getOrderId());
        params.addValue("login", delivery.getLogin());
        params.addValue("account_invoice", delivery.getAccountInvoice());
        params.addValue("description_order", delivery.getDescriptionOrder());
        params.addValue("sum_order", delivery.getSumOrder());
        params.addValue("status", delivery.getStatus());
        jdbc.update("insert into delivery_table (store_id, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status)" +
                        " values (:store_id, :payment_id, :order_id, :login, :account_invoice, :description_order, :sum_order, :status)"
                , params, keyHolder, new String[]{"id"});


        var optionalDelivery = findById(keyHolder.getKeyAs(Long.class));

        return optionalDelivery.orElseThrow(() -> new EntityNotFoundException("Delivery not created"));
    }

    @Override
    public Optional<Delivery> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, store_id, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from delivery_table where id = :id"
                , params, new DeliveryResultSetExtractor()));
    }

    @Override
    public Optional<Delivery> findByUUID(UUID uuid) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_invoice", uuid);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, store_id, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from delivery_table where account_invoice = :account_invoice"
                , params, new DeliveryResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public Optional<Delivery> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, store_id, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from delivery_table where login = :login"
                , params, new DeliveryResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Delivery> findAll() {
        return jdbc.getJdbcOperations().query(
                "select id, created_at, store_id, payment_id, order_id, login," +
                        " account_invoice, description_order, sum_order, status" +
                " from delivery_table", new DeliveryRowMapper());
    }

    @Override
    public void updateDeliveryStatus(long id, Status status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("status", status.toString());
        int result = jdbc.update("update delivery_table set status = :status  where id = :id", params);

        if (result < 1) {
            throw new EntityNotFoundException("The Delivery is not updated");
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class DeliveryResultSetExtractor implements ResultSetExtractor<Delivery> {

        @Override
        public Delivery extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Delivery delivery = new Delivery();

            while (resultSet.next()) {
                delivery.setId(resultSet.getLong("id"));
                delivery.setCreatedAt(resultSet.getTimestamp("created_at"));
                delivery.setStoreId(resultSet.getLong("store_id"));
                delivery.setPaymentId(resultSet.getLong("payment_id"));
                delivery.setOrderId(resultSet.getLong("order_id"));
                delivery.setLogin(resultSet.getString("login"));
                delivery.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                delivery.setDescriptionOrder(resultSet.getString("description_order"));
                delivery.setSumOrder(resultSet.getInt("sum_order"));
                delivery.setStatus(resultSet.getString("status"));
            }

            return delivery;
        }
    }

    private static class DeliveryRowMapper implements RowMapper<Delivery> {

        @Override
        public Delivery mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final Date createdAt = resultSet.getTimestamp("created_at");
            final long storeId = resultSet.getLong("store_id");
            final long paymentId = resultSet.getLong("payment_id");
            final long orderId = resultSet.getLong("order_id");
            final String login = resultSet.getString("login");
            final UUID accountInvoice = UUID.fromString(resultSet.getString("account_invoice"));
            final String descriptionOrder = resultSet.getString("description_order");
            final Integer sumOrder = resultSet.getInt("sum_order");
            final String status = resultSet.getString("status");
            return new Delivery(id, createdAt, storeId, paymentId, orderId, login, accountInvoice,
                    descriptionOrder, sumOrder, status);
        }
    }
}
