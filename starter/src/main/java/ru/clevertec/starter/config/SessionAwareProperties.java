package ru.clevertec.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "session.aware")
public class SessionAwareProperties {

    private boolean enabled;

    private Set<String> blackList = new HashSet<>();

}
