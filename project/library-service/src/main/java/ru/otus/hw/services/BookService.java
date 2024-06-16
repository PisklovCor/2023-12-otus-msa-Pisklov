package ru.otus.hw.services;

import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;

import java.util.List;

public interface BookService {

    List<BookApiDto> findAll();

    BookApiDto create(CreatBookApiDto book);

    BookApiDto findByTitleAndAuthor(String title, String author);

    AccountBookApiDto takeBook(long bookId, long accountId);
}
