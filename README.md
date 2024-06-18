<a href="https://gitmoji.dev">
  <img
    src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
    alt="Gitmoji"
  />
</a>


This is an example Application for a PDA compliant application implemented with the following technologies
### Process Engine
- 5minds processcube
### Frontend
- React 
### Service Contract Implementation Layer
- Quarkus Camel (java)
- Apache ActiveMQ Artemis
### Mocked External Systems 
- S3 (minio)
- SMTP Server

The Services are containerised with Docker

# Getting Started

## Requirements

The application needs the following dependencies installed in developer mode because not everything is dockerized yet. In production, it is possible to run all services in containers and have them managed by an orchestrator like Kubernetes.

- 5minds Studio 1.17 (https://processcube.5minds.de/)
- Docker 26
- Java SDK 21
- .Net 6
- Node.js 18
- Apache Maven (Can also substitute it with the .mvnw wrapper from Quarkus)


## Starting the Application

- cd into `/processSuite/apps/processSuite/frontend` and run `npm install` (only on first startup needed)
- cd into `/processSuite/` and run `docker compose up`
- As soon as the Containers are build and running the process Modells can be redeployed.
  - Connect to running Engine in 5minds Studio: `Connections` <img src="https://github.com/Ainges/BA/assets/81434615/64497420-f9bb-487d-ad6a-d5c5808f4f57" width="25" height="25">
  - Add Connection (+) -> `http://localhost:8000`
  - Navigate to `Explorer` <img src="https://github.com/Ainges/BA/assets/81434615/51ac595f-0d66-44bc-a35c-4e6a5d3888a8](https://github.com/Ainges/BA/assets/81434615/dde9adde-9a9d-44b4-9fc2-77b4199e1e9b" width="25" height="25">
  - Open `/processSuite/processes/`
  - Deploy Solution (Deploys all process modells) `Run` -> `Deploy Solution` -> `Deploy Solution`
- Start the Quarkus / Java Project: cd into `/integration/`
- run `mvn quarkus:dev`

The Integration Component will initialize some Sample Users inside the 5minds Engine as well as in its own Datamodell.
The Logic behind this is also implemented in a process modell, which can be found in `/processSuite/processes/dev` 


