package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.AuthUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcUsersRepository implements UsersRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<AuthUser> findByLoginPassword(String login, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);
        return Optional.ofNullable(jdbc.query(
                "select id, login, password, email, first_name, last_name" +
                        " from auth_user where login = :login and password = :password"
                , params, new UsersResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<AuthUser> findAll() {
        return jdbc.getJdbcOperations()
                .query("select id, login, password, email, first_name, last_name from auth_user",
                        new AuthUserRowMapper());
    }

    @Override
    public AuthUser create(AuthUser authUser) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", authUser.getLogin());
        params.addValue("password", authUser.getPassword());
        params.addValue("email", authUser.getEmail());
        params.addValue("first_name", authUser.getFirstName());
        params.addValue("last_name", authUser.getLastName());
        jdbc.update("insert into auth_user (login, password, email, first_name, last_name)" +
                        " values (:login, :password, :email, :first_name, :last_name)"
                , params, keyHolder, new String[]{"id"});

        authUser.setId(keyHolder.getKeyAs(Long.class));
        return authUser;
    }

    @Override
    public void deleteAccount(long accountId) {
        Map<String, Object> params = Collections.singletonMap("id", accountId);
        jdbc.update("delete from auth_user where id = :id", params);
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
                authUser.setPassword(resultSet.getString("password"));
                authUser.setEmail(resultSet.getString("email"));
                authUser.setFirstName(resultSet.getString("first_name"));
                authUser.setLastName(resultSet.getString("last_name"));
            }

            return authUser;
        }
    }

    private static class AuthUserRowMapper implements RowMapper<AuthUser> {

        @Override
        public AuthUser mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final String login = resultSet.getString("login");
            final String password = resultSet.getString("password");
            final String email = resultSet.getString("email");
            final String firstName = resultSet.getString("first_name");
            final String lastName = resultSet.getString("last_name");
            return new AuthUser(id, login, password, email, firstName, lastName);
        }
    }
}
