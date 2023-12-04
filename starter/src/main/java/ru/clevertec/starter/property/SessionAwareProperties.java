package ru.clevertec.starter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.clevertec.starter.sevice.handler.BlackListHandler;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "session.aware")
public class SessionAwareProperties {

    private String url;
    private Set<String> blackList = new HashSet<>();
    private Set<Class<? extends BlackListHandler>> blackListHandlers = new HashSet<>();

}
