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

# 🏗️ Architecture Diagram
![Architecture](Architecture.png)

---
<p>
📌 Overview

This project implements a real-world microservices architecture using:

🌐 Public API Gateway

🔐 Private Microservices

🔑 Auth0 JWT Authentication

⚙️ Spring Cloud Config Server

🔍 Eureka Server

🔗 Internal REST communication

🛡️ Zero Trust Security Model

All requests must go through API Gateway, and every microservice validates the JWT again.

🧩 Microservices Included
1️⃣ User Service

- Manages user info
- Validates JWT
- Registered with Eureka
- Config fetched from Config Server
- Calls Hotel + Rating internally

2️⃣ Hotel Service

- Manages hotel data
- Communicates with Rating + User services
- Token validation using Auth0 JWT converter

3️⃣ Rating Service

- Stores user ratings
- Aggregates rating data for hotels
- Calls Hotel/User microservices internally

4️⃣ API Gateway (Only Public Component)

- Validates JWT
- Forwards token downstream
- Uses Eureka for routing
- No business logic

5️⃣ Eureka Server

- Registers all services
- Enables dynamic routing
- Ensures scalability

6️⃣ Spring Cloud Config Server

- Centralized configuration
- Backend Git repository
- Serves configs to:

→ User Service

→ Hotel Service

→ Rating Service

→ Gateway

→ Eureka Server

🔐 Authentication Flow (Auth0 JWT)
- Client → Auth0 → Receives JWT  
- Client → API Gateway (JWT validated)
- Gateway → Specific Microservice (JWT validated again)
- Microservices ↔ Internal Communication


- Microservices are private
- Gateway is public
- All internal traffic secured

🌐 End-to-End Request Flow
- Client → Auth0 (Login, JWT)
- Client → API Gateway (Authorize)
- Gateway → Eureka (Find Service)
- Gateway → User/Hotel/Rating (Forward)
- Microservices ↔ Microservices (Internal Calls)

🛠️ Technology Stack
- Component	Technology
- Authentication	Auth0 (JWT / OIDC)
- Gateway	Spring Cloud API Gateway
- Service Registry	Eureka Server
- Configurations	Spring Cloud Config
- Microservices	Spring Boot 3
- Databases	MySQL / MongoDB / PostgreSQL
- Build Tool	Maven
- Language	Java 17
  
📁 Project Structure
- /api-gateway
- /eureka-server
- /config-server
- /user-service
- /hotel-service
- /rating-service
- /assets/diagram.png
- /README.md

🔗 Config Repo
/config-repo
  - application.yml
  - user-service.yml
  - hotel-service.yml
  - rating-service.yml
  - api-gateway.yml
  - eureka-server.yml

🚀 Running the Project (Local Setup)
1️⃣ Start Config Server
cd config-server
mvn spring-boot:run

2️⃣ Start Eureka
cd eureka-server
mvn spring-boot:run

3️⃣ Start Microservices
cd user-service && mvn spring-boot:run
cd hotel-service && mvn spring-boot:run
cd rating-service && mvn spring-boot:run

4️⃣ Start Gateway
cd api-gateway
mvn spring-boot:run

🧪 Testing the APIs

➡️ Every request must include an Auth0 access token

- Postman → Authorization → Bearer Token

- Authorization: Bearer <JWT_TOKEN>


Example:

GET http://localhost:8083/hotels/1

🔒 Production Notes

- Gateway is Public
- Microservices Private
- JWT validated at Gateway + Microservices
- Zero-Trust Architecture
- Centralized Git-backed configuration

✨ Features Demonstrated

→ Modern microservices architecture

→ Secure Auth0 authentication

→ Central config management

→ Service discovery

→ Dynamic routing

→ Internal service communication

→ Token validation

→ Distributed scaling support

📌 Future Enhancements

→ Circuit Breakers (Resilience4j)

→ Distributed Tracing (Zipkin)

→ API Rate Limiting / Throttling

→ Docker + Kubernetes deployment
</p>

<h3 align="center">⭐ If you like this project, don't forget to star the repo!</h3>



