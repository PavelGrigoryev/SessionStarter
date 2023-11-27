package ru.clevertec.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "session.aware")
public class SessionAwareProperties {

    private boolean enabled;

}
