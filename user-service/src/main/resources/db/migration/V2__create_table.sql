CREATE TABLE IF NOT EXISTS profiles.users
(
    id         SERIAL PRIMARY KEY,
    email   VARCHAR(255) NOT NULL UNIQUE,
    firstname varchar(255) NOT NULL ,
    surname varchar(255) NOT NULL
);

