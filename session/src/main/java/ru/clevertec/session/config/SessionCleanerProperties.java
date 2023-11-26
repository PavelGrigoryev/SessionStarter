package ru.clevertec.session.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "session.clean")
public class SessionCleanerProperties {

    private boolean enabled;

}
