-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS BA;

-- Set the search path to the schema
SET search_path TO BA;

-- Create table for employee_status_mapping
CREATE TABLE IF NOT EXISTS employee_status_mapping (
    id SERIAL PRIMARY KEY,
    employment_status VARCHAR(255)
);

-- Insert data into employee_status_mapping
Insert into employee_status_mapping (employment_status) values ('Vollzeit'), ('Teilzeit'), ('Praktikant'), ('Aushilfe'), ('Werksstudent'), ('Ausbildung');

-- Create table for employees
CREATE TABLE IF NOT EXISTS employees (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    pw VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL,
    profile_picture_url VARCHAR(255),
    private_email VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    employment_status INT REFERENCES employee_status_mapping(id)
);


-- Create documents table

CREATE TABLE IF NOT EXISTS documents (
    ID SERIAL PRIMARY KEY,
    document_name VARCHAR(255),
    document_url VARCHAR(255)
);

-- Create table for employee_documents

CREATE TABLE IF NOT EXISTS employee_documents (
    ID SERIAL PRIMARY KEY,
    employee_id INT REFERENCES employees(user_id),
    document_id INT REFERENCES documents(ID),
    document_upload_date DATE
);

-- Create table for one-time passwords
CREATE TABLE IF NOT EXISTS one_time_passwords (
    ID SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    one_time_password VARCHAR(255)
);

