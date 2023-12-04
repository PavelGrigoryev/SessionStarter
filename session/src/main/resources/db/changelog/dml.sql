--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO sessions (login, opening_time)
VALUES ('Alice', '2023-01-01 10:00:00'),
       ('Bob', '2023-01-02 10:30:00'),
       ('Charlie', '2023-01-03 10:45:00');

INSERT INTO black_list(login)
VALUES ('Bob');
