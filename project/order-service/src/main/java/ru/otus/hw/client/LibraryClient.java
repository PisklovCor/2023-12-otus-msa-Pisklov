package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.CreatBookApiOrderDto;
import ru.otus.hw.exceptions.ExternalServiceInteractionException;

@Service
@RequiredArgsConstructor
public class LibraryClient {

    private final WebClient webClient;

    private final PropertiesConfiguration configuration;

    public void sendLibraryServiceBook(CreatBookApiOrderDto dto) {

        webClient
                .post()
                .uri(configuration.getLibraryUrl() + "/api/internal/book/creat")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CreatBookApiOrderDto>() {
                })
                .onErrorMap(WebClientException.class,
                        e -> new ExternalServiceInteractionException("Ошибка отправки сообщения в library-service"))
                .block();
    }
}
