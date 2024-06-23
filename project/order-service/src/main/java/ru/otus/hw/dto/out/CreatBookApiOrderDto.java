package ru.otus.hw.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatBookApiOrderDto {

    private long accountId;

    private String email;

    private String title;

    private String author;

    private Integer rating;
}
