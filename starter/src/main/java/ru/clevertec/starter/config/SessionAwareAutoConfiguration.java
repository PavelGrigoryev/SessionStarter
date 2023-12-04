package ru.clevertec.starter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.clevertec.starter.bpp.SessionAwareBeanPostProcessor;
import ru.clevertec.starter.exception.handler.SessionServiceResponseErrorHandler;
import ru.clevertec.starter.property.SessionAwareProperties;
import ru.clevertec.starter.sevice.SessionAwareService;
import ru.clevertec.starter.sevice.handler.BlackListHandler;
import ru.clevertec.starter.sevice.handler.impl.PropertyBlackListHandler;
import ru.clevertec.starter.sevice.handler.impl.SessionServiceBlackListHandler;

import java.util.List;

@Slf4j
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(SessionAwareProperties.class)
@ConditionalOnClass(SessionAwareProperties.class)
@ConditionalOnProperty(prefix = "session.aware", name = "enabled", havingValue = "true")
public class SessionAwareAutoConfiguration {

    @Bean
    public SessionAwareBeanPostProcessor sessionAwareBeanPostProcessor() {
        return new SessionAwareBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(SessionAwareService.class)
    public SessionAwareService sessionAwareService(RestClient restClient, SessionServiceResponseErrorHandler errorHandler) {
        return new SessionAwareService(restClient, errorHandler);
    }

    @Bean
    public BlackListHandler propertyBlackListHandler(SessionAwareProperties properties) {
        return new PropertyBlackListHandler(properties);
    }

    @Bean
    public BlackListHandler sessionServiceBlackListHandler(SessionAwareService sessionAwareService) {
        return new SessionServiceBlackListHandler(sessionAwareService);
    }

    @Bean
    public SessionServiceResponseErrorHandler sessionServiceResponseErrorHandler() {
        return new SessionServiceResponseErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient(SessionAwareProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeaders(headers -> {
                    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }

    @PostConstruct
    void init() {
        log.info("SessionAwareAutoConfiguration initialized");
    }

}
