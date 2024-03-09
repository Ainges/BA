-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS BA;

-- Set the search path to the schema
SET search_path TO BA;

-- Create table for employees
CREATE TABLE IF NOT EXISTS Employees (
    EmployeeID SERIAL PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    JobTitle VARCHAR(50) NOT NULL,
    ProfileImageURL VARCHAR(255)
);

-- Create table for one-time passwords
CREATE TABLE IF NOT EXISTS OneTimePasswords (
    ID SERIAL PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    OneTimePassword VARCHAR(255)
);
-- Example Employees
INSERT INTO Employees (FirstName, LastName, Email, JobTitle, ProfileImageURL)
VALUES
    ('Michael', 'Scott', 'michael.scott@dundermifflin.com', 'Regionalleiter', '/profilepictures/MichaelScott.png'),
    ('Jim', 'Halpert', 'jim.halpert@dundermifflin.com', 'Vertriebsmitarbeiter', '/profilepictures/JimHalpert.png'),
    ('Pam', 'Beesly', 'pam.beesly@dundermifflin.com', 'Empfangsmitarbeiterin', '/profilepictures/PamBeesly.png'),
    ('Dwight', 'Schrute', 'dwight.schrute@dundermifflin.com', 'Assistent des Regionalleiters', '/profilepictures/DwightSchrute.png'),
    ('Angela', 'Martin', 'angela.martin@dundermifflin.com', 'Leiterin des Partyplanungsausschusses', '/profilepictures/AngelaMartin.png'),
    ('Stanley', 'Hudson', 'stanley.hudson@dundermifflin.com', 'Vertriebsmitarbeiter', '/profilepictures/StandleyHudson.png'),
    ('Phyllis', 'Vance', 'phyllis.vance@dundermifflin.com', 'Vertriebsmitarbeiterin', '/profilepictures/PhyllisVance.png');

