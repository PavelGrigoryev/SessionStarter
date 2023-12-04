package ru.clevertec.starter.sevice.handler.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.starter.property.SessionAwareProperties;
import ru.clevertec.starter.sevice.handler.BlackListHandler;

import java.util.Set;

@RequiredArgsConstructor
public class PropertyBlackListHandler implements BlackListHandler {

    private final SessionAwareProperties sessionAwareProperties;

    @Override
    public Set<String> getBlackList() {
        return sessionAwareProperties.getBlackList();
    }

}
