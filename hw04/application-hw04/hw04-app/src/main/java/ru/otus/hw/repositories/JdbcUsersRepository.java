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
import ru.otus.hw.models.Users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
@RequiredArgsConstructor
public class JdbcUsersRepository implements UsersRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Users> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, login, full_name from users where id = :id"
                , params, new UsersResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Users> findAll() {
        return jdbc.query(
                "select id, login, full_name from users", new BookRowMapper());
    }

    @Override
    public Users save(Users users) {
        if (users.getId() == 0) {
            return insert(users);
        }
        return update(users);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from users where id = :id", params);
    }

    @SuppressWarnings("DataFlowIssue")
    private Users insert(Users users) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", users.getLogin());
        params.addValue("full_name", users.getFullName());
        jdbc.update("insert into users (login, full_name) values (:login, :full_name)"
                , params, keyHolder, new String[]{"id"});

        users.setId(keyHolder.getKeyAs(Long.class));
        return users;
    }

    private Users update(Users users) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", users.getId());
        params.addValue("login", users.getLogin());
        params.addValue("full_name", users.getFullName());
        int result = jdbc.update("update users set login = :login, full_name = :full_name where id = :id", params);

        if (result < 1) {
            throw new EntityNotFoundException("The users is not updated");
        }

        return users;
    }

    private static class BookRowMapper implements RowMapper<Users> {

        @Override
        public Users mapRow(ResultSet resultSet, int rowNum) throws SQLException {

            Users users = new Users();
            users.setId(resultSet.getLong("id"));
            users.setLogin(resultSet.getString("login"));
            users.setFullName(resultSet.getString("full_name"));

            return users;
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class UsersResultSetExtractor implements ResultSetExtractor<Users> {

        @Override
        public Users extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Users users = new Users();

            while (resultSet.next()) {
                users.setId(resultSet.getLong("id"));
                users.setLogin(resultSet.getString("login"));
                users.setFullName(resultSet.getString("full_name"));
            }

            return users;
        }
    }
}
