--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS sessions
(
    id           BIGSERIAL PRIMARY KEY,
    login        VARCHAR(50) NOT NULL UNIQUE,
    opening_time TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS black_list
(
    id    BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE REFERENCES sessions (login) ON DELETE CASCADE
);
