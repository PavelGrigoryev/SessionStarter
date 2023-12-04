package ru.clevertec.starter.sevice;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class SessionServiceBlackListHandler implements BlackListHandler {

    private final SessionAwareService sessionAwareService;

    @Override
    public Set<String> getBlackList() {
        return sessionAwareService.findAllBlackLists().logins();
    }

}
