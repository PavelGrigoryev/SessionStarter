--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS persons
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name     VARCHAR(50) NOT NULL
);
