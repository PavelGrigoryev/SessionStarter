package ru.clevertec.starter.sevice.handler.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.starter.sevice.SessionAwareService;
import ru.clevertec.starter.sevice.handler.BlackListHandler;

import java.util.Set;

@RequiredArgsConstructor
public class SessionServiceBlackListHandler implements BlackListHandler {

    private final SessionAwareService sessionAwareService;

    @Override
    public Set<String> getBlackList() {
        return sessionAwareService.findAllBlackLists().logins();
    }

}
