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

    @PostConstruct
    private void init() {
        log.debug("----параметры----");
        log.debug("destinationSendStore=[{}]", destinationSendStore);
        log.debug("destinationListenerStore=[{}]", destinationListenerStore);
        log.debug("---- ---- ----");
    }
}
