CREATE TABLE IF NOT EXISTS users.users
(
    id         SERIAL PRIMARY KEY,
    email   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    bool DEFAULT false
);



CREATE Table IF NOT EXISTS users.verification_token
(
    id                 SERIAL PRIMARY KEY NOT NULL,
    verification_token varchar UNIQUE,
    user_id            INT                NOT NULL,
    expiration_date    TIMESTAMP
);
