package ru.clevertec.session.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(SessionCleanerProperties.class)
@ConditionalOnClass(SessionCleanerProperties.class)
@ConditionalOnProperty(prefix = "session.clean", name = "enabled", havingValue = "true")
public class SessionCleanerConfig {
}
