package ru.clevertec.starter.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@Data
@EnableScheduling
@ConfigurationProperties(prefix = "session.aware.clean")
@ConditionalOnProperty(prefix = "session.aware.clean", name = "enabled", havingValue = "true")
public class SessionCleanerProperties {

    private boolean enabled;
    private String cron;

}
