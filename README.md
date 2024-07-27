<a href="https://gitmoji.dev">
  <img
    src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
    alt="Gitmoji"
  />
</a>



## Prerequisites

To run the application, the following prerequisites must be met:
- Docker Desktop version 4.29.0 or higher
- DNS redirection of "authority" to 127.0.0.1
- Node.js version 12.22.9 or higher
- npm version 8.8.1 or higher
- Git in the latest version
- (Optional) Postman (to use the templates included in the project)
- (Optional) 5minds Studio version 1.16.1 or higher (to observe process execution)

The DNS redirection can be easily achieved by adding an entry to the host file of the operating system used. 
Even though the entire application runs in Docker, these prerequisites must be met because a Docker image is not hosted in a container registry and will be built upon first start.

## Installation and Starting the Application

- `git clone https://github.com/Ainges/BA`
- Navigate to `./processSuite`
- Install the Node.js packages:
  - `npm install` in `./processSuite/apps/processSuite/frontend`
- Start the application by running: `docker compose up --build` in `./processSuite`

## Usage

The 5minds Portal is available at: `http://localhost:8082`. 
The backend initializes some users at the start. To easily go through the process, you can log in with the following credentials:
- Username: `admin`
- Password: `admin`

In the top left corner, there is a bell icon. Clicking on the bell will bring up the task list to interact with the application. 
Tasks can be assigned in the Process Engine in two ways:
- Direct assignment to the task via UserID
- Through the Swimlane

The admin user can access all tasks, even if they are assigned to a specific user, as is the case in the second phase of the onboarding process. 
Here, tasks are directly assigned to the user ID of the new employee. For this reason, a warning is displayed in the UI when the task is activated by the admin user, but it does not cause any issues.

The 5minds Authority is accessible at `http://authority:11560`. (Hence the DNS redirection)

## Starting the Process

An instance of the onboarding process can be started via a REST request to the backend. 
It is recommended to import the Postman Collection "OnboardingTop PDA" found in `./postmanCollection`. Alternatively, you can send an HTTP POST request to `http://localhost:8080/onboarding/top/employeeHasSignedContract` with the following body:
```json
{
  "first_working_day": "2024-08-01",
  "hours_per_week": 40,
  "first_name": "Max",
  "last_name": "Mustermann",
  "private_email": "max@mustermann.de",
  "position": "HR",
  "birth_date": "2000-08-01",
  "employment_status": "Vollzeit",
  "postal_address": "Musterstraße 12, 12345 Musterstadt"
}
