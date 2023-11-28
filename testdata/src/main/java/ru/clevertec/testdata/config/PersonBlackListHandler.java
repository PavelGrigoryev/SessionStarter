package ru.clevertec.testdata.config;

import org.springframework.stereotype.Component;
import ru.clevertec.starter.sevice.BlackListHandler;

import java.util.Set;

@Component
public class PersonBlackListHandler implements BlackListHandler {

    @Override
    public Set<String> getBlackList() {
        return Set.of("George", "Betsy", "Hulk");
    }

}
