package ru.otus.hw.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.ResponseDto;

@RestController
public class BaseController {

    @GetMapping("/health")
    public ResponseDto getHealth() {
        System.out.println("Запрос на health для проверки контейнера");
        return ResponseDto.builder().status("OK").build();
    }
}
