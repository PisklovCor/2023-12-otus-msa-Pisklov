package ru.otus.hw.services;

import ru.otus.hw.models.AccountBook;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book create(Book book);

    Book findByTitleAndAuthor(String title, String author);

    AccountBook takeBook(long bookId, long accountId);
}
