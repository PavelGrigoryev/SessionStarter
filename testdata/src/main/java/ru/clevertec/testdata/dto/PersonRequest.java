package ru.clevertec.testdata.dto;

import ru.clevertec.starter.model.Login;

public record PersonRequest(String login, String password, String name) implements Login {

    @Override
    public String getLogin() {
        return login;
    }

}
