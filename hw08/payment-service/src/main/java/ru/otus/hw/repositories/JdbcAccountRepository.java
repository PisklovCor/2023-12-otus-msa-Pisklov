package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Date;


@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository implements AccountRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Account create(Account account) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", account.getLogin());
        params.addValue("balance", account.getBalance());
        jdbc.update("insert into account_table (login, balance) values (:login, :balance)"
                , params, keyHolder, new String[]{"id"});

        var optionalAccount = findById(keyHolder.getKeyAs(Long.class));

        return optionalAccount.orElseThrow(() -> new EntityNotFoundException("Account not created"));
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, login, balance from account_table where login = :login"
                , params, new AccountResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Account> findAll() {
        return jdbc.getJdbcOperations().query(
                "select id, created_at, login, balance from account_table", new AccountRowMapper());
    }

    @Override
    public void updateAccount(Account account) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", account.getLogin());
        params.addValue("balance", account.getBalance());
        int result = jdbc.update("update account_table set balance = :balance  where login = :login", params);

        if (result < 1) {
            throw new EntityNotFoundException("The Payment is not updated");
        }
    }

    private Optional<Account> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, created_at, login, balance from account_table where id = :id"
                , params, new AccountResultSetExtractor()));
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class AccountResultSetExtractor implements ResultSetExtractor<Account> {

        @Override
        public Account extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Account account = new Account();

            while (resultSet.next()) {
                account.setId(resultSet.getLong("id"));
                account.setCreatedAt(resultSet.getTimestamp("created_at"));
                account.setLogin(resultSet.getString("login"));
                account.setBalance(resultSet.getInt("balance"));
            }

            return account;
        }
    }

    private static class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final Date createdAt = resultSet.getTimestamp("created_at");
            final String login = resultSet.getString("login");
            final Integer balance = resultSet.getInt("balance");
            return new Account(id, createdAt, login, balance);
        }
    }
}
