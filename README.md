**Gvosy API** is the backend service for the Gvosy mobile application, an AI-powered assistant that helps users structure and develop their thoughts without losing ideas.

The backend is responsible for authentication, data storage, AI processing orchestration, voice note handling, and intelligent search across user notes.

The system is built using a microservices architecture with a strong focus on scalability, reliability, and asynchronous processing.
##### Core Responsibilities
  - User authentication and authorization
  - Storage and management of notes, tasks, and structured data
  - Processing and analysis of voice-based thoughts
  - Orchestration of AI services for:
	  - text structuring
	  - task list generation
	  - contextual suggestions
	  - semantic search across notes
  - Asynchronous event handling and notifications

**Microservices Architecture**
The backend consists of the following services:
  - gateway-service - entry point for all client requests, routing and security
  - auth-service - user authentication, JWT token management, access control
  - main-service - core business logic: notes, tasks, user data
  - ai-service - AI orchestration layer for text analysis, structuring, and search
  - notification-service - event-based notifications and background processing
  - eureka-service - service discovery for internal communication
##### Tech Stack
  - Java 21
  - Spring Boot
  - Spring Cloud Gateway
  - Spring Cloud Eureka
  - PostgreSQL
  - MongoDB
  - Apache Kafka
  - JWT Authentication
  - Docker
##### Data Flow Overview
1. Mobile client sends voice or text data to the API
2. Gateway routes the request to the appropriate service
3. Main service stores raw and structured data
4. AI service processes content and generates insights
5. Results are persisted and returned to the client
6. Events are published via Kafka for further processing
