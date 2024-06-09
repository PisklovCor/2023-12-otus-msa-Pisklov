package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AccountBook;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AccountBookRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AccountBookRepository accountBookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(Book book) {
        log.info("Получена новая книга от пользователя [{}]", book.toString());

        var optionalBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());

        if (optionalBook.isPresent()) {
            log.info("Книга уже сущестует в библиотеке");
            return optionalBook.orElse(null);
        }

        return bookRepository.create(book);
    }

    @Override
    public Book findByTitleAndAuthor(String title, String author) {
        var optionalBook = bookRepository.findByTitleAndAuthor(title, author);

        return optionalBook
                .orElseThrow(() -> new EntityNotFoundException("Books with title %s and author %s not found"
                        .formatted(title, author)));
    }

    @Override
    public AccountBook takeBook(long bookId, long accountId) {
        AccountBook accountBook = new AccountBook();
        accountBook.setAccountId(accountId);
        accountBook.setBookId(bookId);

        return accountBookRepository.create(accountBook);
    }
}
