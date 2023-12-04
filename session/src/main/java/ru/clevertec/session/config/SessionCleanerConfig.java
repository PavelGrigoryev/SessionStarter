package ru.clevertec.session.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "session.clean", name = "enabled", havingValue = "true")
public class SessionCleanerConfig {
}
