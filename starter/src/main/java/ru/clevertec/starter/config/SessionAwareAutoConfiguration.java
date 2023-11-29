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
import ru.clevertec.starter.property.SessionAwareProperties;
import ru.clevertec.starter.property.SessionCleanerProperties;
import ru.clevertec.starter.sevice.BlackListHandler;
import ru.clevertec.starter.sevice.DefaultBlackListHandler;
import ru.clevertec.starter.sevice.SessionAwareService;

import java.util.List;

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
    @ConditionalOnMissingBean(SessionAwareService.class)
    public SessionAwareService sessionAwareService(RestClient restClient) {
        return new SessionAwareService(restClient);
    }

    @Bean
    public BlackListHandler blackListHandler() {
        return new DefaultBlackListHandler();
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
