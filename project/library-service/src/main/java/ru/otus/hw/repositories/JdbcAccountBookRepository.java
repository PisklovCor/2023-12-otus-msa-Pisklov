package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.AccountBook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAccountBookRepository implements AccountBookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<AccountBook> findAll() {
        return jdbc.getJdbcOperations()
                .query("select id, account_id, book_id from account_book_table", new AccountBookRowMapper());
    }


    @Override
    public Optional<AccountBook> findByAccountId(long accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return Optional.ofNullable(jdbc.query(
                "select id, account_id, book_id from account_book_table where account_id = :account_id"
                , params, new AccountBookResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public AccountBook create(AccountBook accountBook) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("account_id", accountBook.getAccountId());
        params.addValue("book_id", accountBook.getBookId());
        jdbc.update("insert into account_book_table (account_id, book_id)" +
                        " values (:account_id, :book_id)"
                , params, keyHolder, new String[]{"id"});

        AccountBook newAccountBook = new AccountBook();
        newAccountBook.setId(keyHolder.getKeyAs(Long.class));
        newAccountBook.setAccountId(accountBook.getAccountId());
        newAccountBook.setBookId(accountBook.getBookId());
        return newAccountBook;
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class AccountBookResultSetExtractor implements ResultSetExtractor<AccountBook> {

        @Override
        public AccountBook extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            AccountBook accountBook = new AccountBook();

            while (resultSet.next()) {
                accountBook.setId(resultSet.getLong("id"));
                accountBook.setAccountId(resultSet.getLong("account_id"));
                accountBook.setBookId(resultSet.getLong("book_id"));
            }

            return accountBook;
        }
    }

    private static class AccountBookRowMapper implements RowMapper<AccountBook> {

        @Override
        public AccountBook mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final long accountId = resultSet.getLong("account_id");
            final long bookId = resultSet.getLong("book_id");
            return new AccountBook(id, accountId, bookId);
        }
    }
}
