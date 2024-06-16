package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long bookId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", bookId);
        return Optional.ofNullable(jdbc.query(
                "select id, title, author, rating from book_table where id = :id"
                , params, new BookResultSetExtractor())).filter(b -> b.getId() != 0);
    }

    @Override
    public List<Book> findAll() {
        return jdbc.getJdbcOperations()
                .query("select id, title, author, rating from book_table", new BookRowMapper());
    }

    @Override
    public Book create(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author", book.getAuthor());
        params.addValue("rating", book.getRating());
        jdbc.update("insert into book_table (title, author, rating)" +
                        " values (:title, :author, :rating)"
                , params, keyHolder, new String[]{"id"});

        Book newBook = new Book();
        newBook.setId(keyHolder.getKeyAs(Long.class));
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setRating(book.getRating());
        return newBook;
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("author", author);
        return Optional.ofNullable(jdbc.query(
                "select id, title, author, rating from book_table where title = :title and author = :author"
                , params, new BookResultSetExtractor())).filter(b -> b.getId() != 0);
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Book book = new Book();

            while (resultSet.next()) {
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setRating(resultSet.getInt("rating"));
            }

            return book;
        }
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            final long id = resultSet.getLong("id");
            final String title = resultSet.getString("title");
            final String author = resultSet.getString("author");
            final Integer rating = resultSet.getInt("rating");
            return new Book(id, title, author, rating);
        }
    }
}
