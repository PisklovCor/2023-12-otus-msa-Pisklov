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
    private String destinationSendDelivery;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationListenerDelivery;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationSendPayment;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationListenerPayment;

    @PostConstruct
    private void init() {
        log.debug("----параметры----");
        log.debug("destinationSendDelivery=[{}]", destinationSendDelivery);
        log.debug("destinationListenerDelivery=[{}]", destinationListenerDelivery);
        log.debug("destinationSendPayment=[{}]", destinationSendPayment);
        log.debug("destinationListenerPayment=[{}]", destinationListenerPayment);
        log.debug("---- ---- ----");
    }
}
