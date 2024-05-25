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
import ru.otus.hw.models.Payment;

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
public class JdbcPaymentRepository implements PaymentRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Payment create(Payment payment) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_id", payment.getOrderId());
        params.addValue("login", payment.getLogin());
        params.addValue("account_invoice", payment.getAccountInvoice());
        params.addValue("description_order", payment.getDescriptionOrder());
        params.addValue("sum_order", payment.getSumOrder());
        params.addValue("status", payment.getStatus());
        jdbc.update("insert into payment_table (order_id, login, account_invoice," +
                        " description_order, sum_order, status)" +
                        " values (:order_id, :login, :account_invoice, :description_order, :sum_order, :status)"
                , params, keyHolder, new String[]{"id"});


        var optionalPayment = findById(keyHolder.getKeyAs(Long.class));

        return optionalPayment.orElseThrow(() -> new EntityNotFoundException("Payment not created"));
    }

    @Override
    public Optional<Payment> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, order_id, login, account_invoice, description_order, sum_order, status" +
                        " from payment_table where id = :id"
                , params, new PaymentResultSetExtractor()));
    }

    @Override
    public Optional<Payment> findByUUID(UUID uuid) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_invoice", uuid);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, order_id, login, account_invoice, description_order, sum_order, status" +
                        " from payment_table where account_invoice = :account_invoice"
                , params, new PaymentResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public Optional<Payment> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, order_id, login, account_invoice, description_order, sum_order, status" +
                        " from payment_table where login = :login"
                , params, new PaymentResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Payment> findAll() {
        return jdbc.getJdbcOperations().query(
                "select id, created_at, order_id, login, account_invoice, description_order, sum_order, status" +
                " from payment_table", new PaymentRowMapper());
    }

    @Override
    public void updatePaymentStatus(long id, Status status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("status", status.toString());
        int result = jdbc.update("update payment_table set status = :status  where id = :id", params);

        if (result < 1) {
            throw new EntityNotFoundException("The Payment is not updated");
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class PaymentResultSetExtractor implements ResultSetExtractor<Payment> {

        @Override
        public Payment extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Payment payment = new Payment();

            while (resultSet.next()) {
                payment.setId(resultSet.getLong("id"));
                payment.setCreatedAt(resultSet.getTimestamp("created_at"));
                payment.setOrderId(resultSet.getLong("order_id"));
                payment.setLogin(resultSet.getString("login"));
                payment.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                payment.setDescriptionOrder(resultSet.getString("description_order"));
                payment.setSumOrder(resultSet.getInt("sum_order"));
                payment.setStatus(resultSet.getString("status"));
            }

            return payment;
        }
    }

    private static class PaymentRowMapper implements RowMapper<Payment> {

        @Override
        public Payment mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final Date createdAt = resultSet.getTimestamp("created_at");
            final long orderId = resultSet.getLong("order_id");
            final String login = resultSet.getString("login");
            final UUID accountInvoice = UUID.fromString(resultSet.getString("account_invoice"));
            final String descriptionOrder = resultSet.getString("description_order");
            final Integer sumOrder = resultSet.getInt("sum_order");
            final String status = resultSet.getString("status");
            return new Payment(id, createdAt, orderId, login, accountInvoice, descriptionOrder, sumOrder, status);
        }
    }
}
