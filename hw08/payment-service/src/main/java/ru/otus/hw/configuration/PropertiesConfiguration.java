package ru.otus.hw.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@ConfigurationProperties(prefix = "application")
public class PropertiesConfiguration {

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationSendStore;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationListenerStore;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationSendOrder;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationListenerOrder;

    @PostConstruct
    private void init() {
        log.debug("----параметры----");
        log.debug("destinationSend=[{}]", destinationSendStore);
        log.debug("destinationListener=[{}]", destinationListenerStore);
        log.debug("destinationSendOrder=[{}]", destinationSendOrder);
        log.debug("destinationListenerOrder=[{}]", destinationListenerOrder);
        log.debug("---- ---- ----");
    }
}
