package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.ArtemisProducer;
import ru.otus.hw.client.OrderClient;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.AccountAllBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;
import ru.otus.hw.dto.in.CreatBookApiOrderDto;
import ru.otus.hw.dto.out.NotificationMessage;
import ru.otus.hw.dto.out.OrderDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AccountBook;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AccountBookRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AccountBookRepository accountBookRepository;

    private final BookConverter converter;

    private final OrderClient orderClient;

    private final ArtemisProducer artemisProducer;

    @Override
    public List<BookApiDto> findAll() {
        return bookRepository.findAll().stream().map(converter::toDto).toList();
    }

    @Override
    public BookApiDto create(CreatBookApiDto book, long accountId, String email) {
        log.info("Получена новая книга от пользователя [{}]", book.toString());
        var optionalBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (optionalBook.isPresent()) {
            log.info("Книга уже сущестует в библиотеке");
            return converter.toDto(optionalBook.orElse(null));
        }

        BookApiDto bookApiDto = converter.toDto(bookRepository.create(converter.toModel(book)));
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setAccountId(accountId);
        notificationMessage.setEmail(email);
        notificationMessage.setMessage("Вы добавили новую книгу. Спасибо.");
        notificationMessage.setTitle(bookApiDto.getTitle());
        notificationMessage.setAuthor(bookApiDto.getAuthor());
        log.info("Отправка из сервиса в очередь");
        sendJmsMessage(notificationMessage);
        return bookApiDto;
    }

    @Override
    public BookApiDto createInternal(CreatBookApiOrderDto dto) {
        log.info("Получена новая книга от пользователя [{}]", dto);
        var optionalBook = bookRepository.findByTitleAndAuthor(dto.getTitle(), dto.getAuthor());
        if (optionalBook.isPresent()) {
            log.info("Книга уже сущестует в библиотеке");
            return converter.toDto(optionalBook.orElse(null));
        }

        BookApiDto bookApiDto = converter.toDto(bookRepository.create(converter.toModelInternalDto(dto)));
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setAccountId(dto.getAccountId());
        notificationMessage.setEmail(dto.getEmail());
        notificationMessage.setMessage("По вашему запросу книга добавлена. Списибо за ожидание.");
        notificationMessage.setTitle(bookApiDto.getTitle());
        notificationMessage.setAuthor(bookApiDto.getAuthor());
        log.info("Отправка из сервиса в очередь");
        sendJmsMessage(notificationMessage);
        return bookApiDto;
    }

    @Override
    public BookApiDto findByTitleAndAuthor(String title, String author) {
        var optionalBook = bookRepository.findByTitleAndAuthor(title, author);

        return converter.toDto(optionalBook
                .orElseThrow(() -> new EntityNotFoundException("Books with title %s and author %s not found"
                        .formatted(title, author))));
    }

    @Override
    public AccountBookApiDto takeBook(long bookId, long accountId, String email) {
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
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setAccountId(accountId);
        notificationMessage.setEmail(email);
        notificationMessage.setMessage("Вы получили книгу.");
        notificationMessage.setTitle(book.get().getTitle());
        notificationMessage.setAuthor(book.get().getAuthor());
        log.info("Отправка из сервиса в очередь");
        sendJmsMessage(notificationMessage);

        return dto;
    }

    @Override
    public AccountAllBookApiDto getBookByAccount(long accountId) {

        var optionalAccountAll = accountBookRepository.findAll();

        var optionalAccountById = optionalAccountAll.stream().filter(a -> a.getAccountId() == accountId).toList();

        List<Book> bookList = new ArrayList<>();

        for (AccountBook accountBook : optionalAccountById) {
            Optional<Book> book = bookRepository.findById(accountBook.getBookId());

            if (book.isEmpty()) {
                throw new EntityNotFoundException("Books with id %s not found".formatted(accountBook.getBookId()));
            }

            bookList.add(book.get());
        }

        AccountAllBookApiDto dto = new AccountAllBookApiDto();
        dto.setAccountId(accountId);
        dto.setBook(bookList.stream().map(converter::toDto).toList());
        return dto;
    }

    @Override
    public void leaveRequestForABook(CreatBookApiDto book, long accountId, String email) {
        log.info("Книга будет отправлена на заказ для пополнения!");

        OrderDto orderDto = new OrderDto();
        orderDto.setAccountId(accountId);
        orderDto.setEmail(email);
        orderDto.setTitle(book.getTitle());
        orderDto.setAuthor(book.getAuthor());

        orderClient.sendOrderServiceBook(orderDto);
        log.info("Запрос успешно отправлен");

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setAccountId(accountId);
        notificationMessage.setEmail(email);
        notificationMessage.setMessage("Ваша заявка успешно создана. Ожидайте пополнения.");
        notificationMessage.setTitle(book.getTitle());
        notificationMessage.setAuthor(book.getAuthor());

        log.info("Отправка из сервиса в очередь");
        sendJmsMessage(notificationMessage);
    }

    @Override
    public void deleteBook(long bookId) {
        bookRepository.deleteBook(bookId);
    }

    private void sendJmsMessage(NotificationMessage message) {
        artemisProducer.sendMessage(message);
    }
}
