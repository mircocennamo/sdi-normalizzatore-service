package it.interno.normalizzatore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:messages.properties")
public class ApplicationProperties {

    @Value("${divitech.url}")
    private String divitechUrl;

    public String getDivitechUrl() {
        return divitechUrl;
    }
}
