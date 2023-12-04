package ru.clevertec.starter.sevice;

import lombok.RequiredArgsConstructor;
import ru.clevertec.starter.property.SessionAwareProperties;

import java.util.Set;

@RequiredArgsConstructor
public class PropertyBlackListHandler implements BlackListHandler {

    private final SessionAwareProperties sessionAwareProperties;

    @Override
    public Set<String> getBlackList() {
        return sessionAwareProperties.getBlackList();
    }

}
