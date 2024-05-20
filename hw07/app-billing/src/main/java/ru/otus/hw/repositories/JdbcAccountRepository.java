package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcAccountRepository implements AccountRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Account createAccount(Account account) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", account.getLogin());
        params.addValue("money", account.getMoney());
        params.addValue("full_name", account.getFullName());
        jdbc.update("insert into account (login, money, full_name)" +
                        " values (:login, :money, :full_name)"
                , params, keyHolder, new String[]{"id"});

        account.setId(keyHolder.getKeyAs(Long.class));
        return account;
    }

    @Override
    public Optional<Account> findAccountByLogin(String login) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        return Optional.ofNullable(jdbc.query(
                "select id, login, invoice, money, full_name from account where login = :login"
                , params, new AccountResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    public Account updateMoney(Account account, Integer money) {

        Integer originalMoney = account.getMoney();
        Integer updateMoney = originalMoney + money;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", account.getLogin());
        params.addValue("money", updateMoney);

        jdbc.update("update account set money = :money where login = :login"
                , params);

        return Optional.ofNullable(jdbc.query(
                "select id, login, invoice, money, full_name from account where login = :login"
                , params, new AccountResultSetExtractor())).filter(b -> b.getId() != 0).get();
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class AccountResultSetExtractor implements ResultSetExtractor<Account> {

        @Override
        public Account extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Account account = new Account();

            while (resultSet.next()) {
                account.setId(resultSet.getLong("id"));
                account.setLogin(resultSet.getString("login"));
                account.setInvoice(UUID.fromString(resultSet.getString("invoice")));
                account.setMoney(resultSet.getInt("money"));
                account.setFullName(resultSet.getString("full_name"));
            }

            return account;
        }
    }
}
