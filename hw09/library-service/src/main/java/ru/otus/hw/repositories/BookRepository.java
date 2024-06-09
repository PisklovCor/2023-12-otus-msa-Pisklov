package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Book create(Book book);

    Optional<Book> findByTitleAndAuthor(String title, String author);

}
