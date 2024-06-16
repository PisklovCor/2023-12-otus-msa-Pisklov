package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AccountBook;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AccountBookRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AccountBookRepository accountBookRepository;

    private final BookConverter converter;

    @Override
    public List<BookApiDto> findAll() {
        return bookRepository.findAll().stream().map(converter::toDto).toList();
    }

    @Override
    public BookApiDto create(CreatBookApiDto book) {
        log.info("Получена новая книга от пользователя [{}]", book.toString());

        var optionalBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());

        if (optionalBook.isPresent()) {
            log.info("Книга уже сущестует в библиотеке");
            return converter.toDto(optionalBook.orElse(null));
        }

        return converter.toDto(bookRepository.create(converter.toModel(book)));
    }

    @Override
    public BookApiDto findByTitleAndAuthor(String title, String author) {
        var optionalBook = bookRepository.findByTitleAndAuthor(title, author);

        return converter.toDto(optionalBook
                .orElseThrow(() -> new EntityNotFoundException("Books with title %s and author %s not found"
                        .formatted(title, author))));
    }

    @Override
    public AccountBookApiDto takeBook(long bookId, long accountId) {
        AccountBook accountBook = new AccountBook();
        accountBook.setAccountId(accountId);
        accountBook.setBookId(bookId);

        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty()) {
            throw new EntityNotFoundException("Books with id %s not found".formatted(bookId));
        }

        AccountBook model = accountBookRepository.create(accountBook);

        AccountBookApiDto dto = new AccountBookApiDto();
        dto.setId(model.getId());
        dto.setAccountId(model.getAccountId());
        dto.setBook(converter.toDto(book.orElse(null)));

        return dto;
    }
}
