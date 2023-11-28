package ru.clevertec.starter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import ru.clevertec.starter.bpp.SessionAwareBeanPostProcessor;
import ru.clevertec.starter.property.SessionAwareProperties;
import ru.clevertec.starter.property.SessionCleanerProperties;
import ru.clevertec.starter.sevice.BlackListHandler;
import ru.clevertec.starter.sevice.DefaultBlackListHandler;
import ru.clevertec.starter.sevice.SessionService;

@Slf4j
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties({SessionAwareProperties.class, SessionCleanerProperties.class})
@ConditionalOnClass({SessionAwareProperties.class, SessionCleanerProperties.class})
@ConditionalOnProperty(prefix = "session.aware", name = "enabled", havingValue = "true")
public class SessionAwareAutoConfiguration {

    @Bean
    public SessionAwareBeanPostProcessor sessionAwareBeanPostProcessor() {
        return new SessionAwareBeanPostProcessor();
    }

    @Bean
    public SessionService sessionService() {
        return new SessionService();
    }

    @Bean
    public BlackListHandler blackListHandler() {
        return new DefaultBlackListHandler();
    }

    @PostConstruct
    void init() {
        log.info("SessionAwareAutoConfiguration initialized");
    }

}
