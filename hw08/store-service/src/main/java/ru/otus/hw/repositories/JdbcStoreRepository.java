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
import ru.otus.hw.models.Store;

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
public class JdbcStoreRepository implements StoreRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Store create(Store store) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("payment_id", store.getPaymentId());
        params.addValue("order_id", store.getOrderId());
        params.addValue("login", store.getLogin());
        params.addValue("account_invoice", store.getAccountInvoice());
        params.addValue("description_order", store.getDescriptionOrder());
        params.addValue("sum_order", store.getSumOrder());
        params.addValue("status", store.getStatus());
        jdbc.update("insert into store_table (payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status)" +
                        " values (:payment_id, :order_id, :login, :account_invoice, :description_order, :sum_order, :status)"
                , params, keyHolder, new String[]{"id"});


        var optionalStore = findById(keyHolder.getKeyAs(Long.class));

        return optionalStore.orElseThrow(() -> new EntityNotFoundException("Store not created"));
    }

    @Override
    public Optional<Store> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from store_table where id = :id"
                , params, new StoreResultSetExtractor()));
    }

    @Override
    public Optional<Store> findByUUID(UUID uuid) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_invoice", uuid);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from store_table where account_invoice = :account_invoice"
                , params, new StoreResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public Optional<Store> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, payment_id, order_id, login, account_invoice," +
                        " description_order, sum_order, status" +
                        " from store_table where login = :login"
                , params, new StoreResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Store> findAll() {
        return jdbc.getJdbcOperations().query(
                "select id, created_at, payment_id, order_id, login," +
                        " account_invoice, description_order, sum_order, status" +
                " from store_table", new StoreRowMapper());
    }

    @Override
    public void updateStoreStatus(long id, Status status) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("status", status.toString());
        int result = jdbc.update("update store_table set status = :status  where id = :id", params);

        if (result < 1) {
            throw new EntityNotFoundException("The Store is not updated");
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class StoreResultSetExtractor implements ResultSetExtractor<Store> {

        @Override
        public Store extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Store store = new Store();

            while (resultSet.next()) {
                store.setId(resultSet.getLong("id"));
                store.setCreatedAt(resultSet.getTimestamp("created_at"));
                store.setPaymentId(resultSet.getLong("payment_id"));
                store.setOrderId(resultSet.getLong("order_id"));
                store.setLogin(resultSet.getString("login"));
                store.setAccountInvoice(UUID.fromString(resultSet.getString("account_invoice")));
                store.setDescriptionOrder(resultSet.getString("description_order"));
                store.setSumOrder(resultSet.getInt("sum_order"));
                store.setStatus(resultSet.getString("status"));
            }

            return store;
        }
    }

    private static class StoreRowMapper implements RowMapper<Store> {

        @Override
        public Store mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final Date createdAt = resultSet.getTimestamp("created_at");
            final long paymentId = resultSet.getLong("payment_id");
            final long orderId = resultSet.getLong("order_id");
            final String login = resultSet.getString("login");
            final UUID accountInvoice = UUID.fromString(resultSet.getString("account_invoice"));
            final String descriptionOrder = resultSet.getString("description_order");
            final Integer sumOrder = resultSet.getInt("sum_order");
            final String status = resultSet.getString("status");
            return new Store(id, createdAt,paymentId, orderId, login, accountInvoice,
                    descriptionOrder, sumOrder, status);
        }
    }
}
