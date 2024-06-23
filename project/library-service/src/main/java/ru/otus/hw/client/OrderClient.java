package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.OrderDto;
import ru.otus.hw.exceptions.ExternalServiceInteractionException;

@Service
@RequiredArgsConstructor
public class OrderClient {

    private final WebClient webClient;

    private final PropertiesConfiguration configuration;

    public void sendOrderServiceBook(OrderDto dto) {

        webClient
                .post()
                .uri(configuration.getOrderUrl() + "/api/order/create")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OrderDto>() {
                })
                .onErrorMap(WebClientException.class,
                        e -> new ExternalServiceInteractionException("Ошибка отправки сообщения в order-service"))
                .block();
    }
}
