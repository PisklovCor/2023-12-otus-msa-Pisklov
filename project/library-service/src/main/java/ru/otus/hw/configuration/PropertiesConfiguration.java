package ru.otus.hw.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@ConfigurationProperties(prefix = "application")
public class PropertiesConfiguration {

    @NotBlank
    private String orderUrl;

    @NotBlank
    private String destinationSend;

    @PostConstruct
    private void init() {
        log.debug("----параметры----");
        log.debug("accountUrl=[{}]", orderUrl);
        log.debug("destinationSend=[{}]", destinationSend);
        log.debug("---- ---- ----");
    }
}
