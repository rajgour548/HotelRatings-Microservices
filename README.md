<h1 align="center">🏨 Hotel Ratings Microservices Architecture</h1>

<p align="center">
A secure, production-style microservice system built using Spring Boot, Java 17, Auth0, Eureka, API Gateway, Config Server, and inter-service communication.
</p>

<p align="center">
  <!-- Tech Stack -->
  <img src="https://img.shields.io/badge/Java-17-orange" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5-green" />
  <img src="https://img.shields.io/badge/Microservices-Architecture-blue" />
  <img src="https://img.shields.io/badge/Security-JWT-yellow" />
  <img src="https://img.shields.io/badge/Auth-Auth0-red" />
  <img src="https://img.shields.io/badge/Discovery-Eureka-lightgrey" />
  <img src="https://img.shields.io/badge/Config%20Server-Spring%20Cloud-purple" />
  <img src="https://img.shields.io/badge/Gateway-Spring%20Cloud%20Gateway-darkgreen" />
</p>

<p align="center">
  <!-- Repo Stats -->
  <img src="https://img.shields.io/github/stars/rajgour/Hotel-Ratings-Microservices?style=flat&color=yellow" />
  <img src="https://img.shields.io/github/forks/rajgour/Hotel-Ratings-Microservices?style=flat&color=blue" />
  <img src="https://img.shields.io/github/issues/rajgour/Hotel-Ratings-Microservices?color=red" />
  <img src="https://img.shields.io/github/license/rajgour/Hotel-Ratings-Microservices?color=lightgrey" />
</p>

---
📌 Overview

This project is a secure microservices-based system built with Spring Boot, providing hotel rating functionalities using a modern distributed architecture.

It follows real-world production patterns:

API Gateway (Public)

Private Microservices

Auth0 JWT Authentication

Eureka Service Registry

Spring Cloud Config Server

Inter-Service Internal Communication

Token validation at Gateway + microservices

🏗️ Architecture Diagram

(Place your final generated diagram here)
Example:
![Architecture](./Architecture.png)


🧩 Microservices Included
1️⃣ User Service

Manages user details

Issues no auth — only verifies JWT

Registers with Eureka

Gets configuration from Config Server

Internal communication with Hotel & Rating services

2️⃣ Hotel Service

Manages hotel information

Communicates internally with Rating Service & User Service

All requests validated using JWT (via Keycloak/Okta/Auth0 converter)

3️⃣ Rating Service

Stores ratings for hotels by users

Provides rating aggregation APIs

Internal REST calls to Hotel/User services

4️⃣ API Gateway (Public Entry Point)

Only public-facing component

Validates Auth0-issued JWT

Forwards authenticated requests to microservices

Uses Eureka to dynamically route to services

Adds no additional business logic

5️⃣ Eureka Server (Service Discovery)

All microservices register here

API Gateway uses Eureka for load balancing

Allows dynamic scaling of services

6️⃣ Spring Cloud Config Server

Provides centralized configuration to:

User Service

Hotel Service

Rating Service

API Gateway

Eureka Server

Loads configs from GitHub configuration repository

🔐 Authentication & Authorization (Auth0)

Clients authenticate with Auth0

Receive JWT Access Token

Token is passed in Authorization: Bearer <token> header

API Gateway validates token

Gateway forwards token to microservices

Microservices again validate token + roles

No service accepts traffic directly from internet

This ensures zero-trust, secure backend.

🌐 Request Flow (End-to-End)

1️⃣ Client → Auth0
User authenticates → receives JWT.

2️⃣ Client → Public API Gateway
Client calls an endpoint using JWT.
Gateway verifies token and forwards request.

3️⃣ Gateway → Eureka
Gateway looks up the correct microservice.

4️⃣ Gateway → Specific Microservice
Routes request internally to User/Hotel/Rating service.
Microservice again validates JWT + roles.

5️⃣ Microservices → Other Microservices
Internal communication happens for aggregated responses.

🛠️ Technology Stack
Layer	Technology
Client Authentication	Auth0 (OIDC, JWT)
Routing	Spring Cloud API Gateway
Service Discovery	Eureka Server
Configurations	Spring Cloud Config Server + Git Repo
Services	Spring Boot 3 (Web, Data, Security, Actuator)
Security	Spring Security + Auth0 JWT Converter
Database	Any (MongoDB, PostgreSQL, MySQL depending on service)
Build Tool	Maven
Language	Java 17
📁 Project Structure
/api-gateway
/eureka-server
/config-server
/user-service
/hotel-service
/rating-service


Your configuration repo:

/config-repo
  - application.yml
  - user-service.yml
  - hotel-service.yml
  - rating-service.yml
  - api-gateway.yml
  - eureka-server.yml

🚀 Running the Project (Local Setup)
Step 1 — Start Config Server
cd config-server
mvn spring-boot:run

Step 2 — Start Eureka Server
cd eureka-server
mvn spring-boot:run

Step 3 — Start Microservices

Order doesn’t matter (Eureka auto-reconnects)

cd user-service && mvn spring-boot:run
cd hotel-service && mvn spring-boot:run
cd rating-service && mvn spring-boot:run

Step 4 — Start API Gateway
cd api-gateway
mvn spring-boot:run

🧪 Testing the APIs

You must send JWT token from Auth0.

Use Postman/ThunderClient:

Authorization → Bearer <your_access_token>


Example endpoint:

GET http://localhost:8083/hotels/1

🔒 Production Notes

✔ API Gateway is public
✔ All microservices are private
✔ JWT validated twice (gateway + services)
✔ Communication is service-to-service only
✔ No microservice exposed to external network
✔ Configs stored in external Git-based config store

✨ Features Demonstrated

Real-world distributed architecture

Secure Zero-Trust backend system

Centralized configuration

Service discovery

Token-based security

Clean layered code structure

Inter-microservice communication

📌 Future Enhancements

Add Circuit Breakers (Resilience4j)

Add Distributed Tracing (Zipkin)

Add API Rate Limiting

Add Kubernetes deployment manifests


