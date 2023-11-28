package ru.clevertec.starter.sevice;

import java.util.Set;

public class DefaultBlackListHandler implements BlackListHandler {

    @Override
    public Set<String> getBlackList() {
        return Set.of("Ashley", "John", "KILLER-666");
    }

}
