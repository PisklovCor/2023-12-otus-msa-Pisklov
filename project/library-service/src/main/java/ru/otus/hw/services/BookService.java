package ru.otus.hw.services;

import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.AccountAllBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;

import java.util.List;

public interface BookService {

    List<BookApiDto> findAll();

    BookApiDto create(CreatBookApiDto book, long accountId, String email);

    BookApiDto findByTitleAndAuthor(String title, String author);

    AccountBookApiDto takeBook(long bookId, long accountId, String email);

    AccountAllBookApiDto getBookByAccount(long accountId);

    void leaveRequestForABook(CreatBookApiDto book, long accountId, String email);
}
