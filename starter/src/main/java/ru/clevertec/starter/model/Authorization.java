package ru.clevertec.starter.model;

public record Authorization(String login) implements Login {

    @Override
    public String getLogin() {
        return login;
    }

}
