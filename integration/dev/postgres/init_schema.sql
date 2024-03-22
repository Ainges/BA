-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS BA;

-- Set the search path to the schema
SET search_path TO BA;

-- Create table for employees
CREATE TABLE IF NOT EXISTS canonicalusers (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL,
    profile_picture_url VARCHAR(255)
);

-- Create table for one-time passwords
CREATE TABLE IF NOT EXISTS OneTimePasswords (
    ID SERIAL PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    OneTimePassword VARCHAR(255)
);

