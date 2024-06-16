package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookConverter {

    public BookApiDto toDto(Book book) {
        BookApiDto dto = new BookApiDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setRating(book.getRating());
        return  dto;
    }

    public Book toModel(CreatBookApiDto dto) {
        Book model = new Book();
        model.setTitle(dto.getTitle());
        model.setAuthor(dto.getAuthor());
        model.setRating(dto.getRating());
        return model;
    }
}
