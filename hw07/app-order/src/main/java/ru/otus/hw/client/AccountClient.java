package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.exceptions.ExternalServiceInteractionException;

@Service
@RequiredArgsConstructor
public class AccountClient {

    private final WebClient webClient;

    private final PropertiesConfiguration configuration;

    public AccountDto getAccountInfo(String login) {

        return webClient
                .get()
                .uri(configuration.getAccountUrl() + "/api/account/" + login)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AccountDto>() {
                })
                .onErrorMap(WebClientException.class,
                        e -> new ExternalServiceInteractionException("Ошибка получения данных от биллинга")).block();
    }

    public AccountDto withdrawal(String login, Integer sumOrder) {

        final Integer sumOrderForBilling = -sumOrder;

        return webClient
                .put()
                .uri(configuration.getAccountUrl() + "/api/refill/" + login)
                .bodyValue(sumOrderForBilling)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AccountDto>() {
                })
                .onErrorMap(WebClientException.class,
                        e -> new ExternalServiceInteractionException("Ошибка снятия денегв сервисе биллинга")).block();
    }
}
