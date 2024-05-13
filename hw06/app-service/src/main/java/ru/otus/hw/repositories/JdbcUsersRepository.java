package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.AuthUser;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
@RequiredArgsConstructor
public class JdbcUsersRepository implements UsersRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<AuthUser> findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", new BigInteger(id));
        return Optional.ofNullable(jdbc.query(
                "select id, login, age from user_profile where id = :id"
                , params, new UsersResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public AuthUser create(String id, String login) {
        var keyHolder = new GeneratedKeyHolder();

        final int randomNum = (int)(Math.random() * 10) + 1;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", new BigInteger(id));
        params.addValue("login", login);
        params.addValue("age", randomNum);
        jdbc.update("insert into user_profile (id, login, age) values (:id, :login, :age)"
                , params, keyHolder, new String[]{"id"});

        AuthUser authUser = new AuthUser();
        authUser.setAge(randomNum);
        authUser.setLogin(login);
        authUser.setId(keyHolder.getKeyAs(Long.class));
        return authUser;
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class UsersResultSetExtractor implements ResultSetExtractor<AuthUser> {

        @Override
        public AuthUser extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            AuthUser authUser = new AuthUser();

            while (resultSet.next()) {
                authUser.setId(resultSet.getLong("id"));
                authUser.setLogin(resultSet.getString("login"));
                authUser.setAge(resultSet.getInt("age"));
            }

            return authUser;
        }
    }
}
