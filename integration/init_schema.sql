CREATE SCHEMA IF NOT EXISTS BA;

CREATE TABLE IF NOT EXISTS BA.oneTimePasswords (
  firstname VARCHAR(255),
  lastname VARCHAR(255),
  oneTimePassword VARCHAR(255) PRIMARY KEY
);