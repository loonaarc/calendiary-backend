How to run:
- This project uses Java 21. Make sure Java 21 is installed: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html
  - Makse sue Java 21 ist set as JAVA_HOME under system variables
  - Refresh maven restart IntellIj if necessary
- Make sure Docker is installed and runinng https://www.docker.com/products/docker-desktop/
- Run docker.compose.yml file
- Start Application

Make sure these Intellij Plugins are installed:
- Lombok
- Docker

Settings:
Settings > Build > Compiler > Annotation Processors
✔ Enable annotation processing


my bearer token for:
- lilie@test.com
- test123
- eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaWxpZUB0ZXN0LmNvbSIsImlhdCI6MTc0ODAzMTA3NiwiZXhwIjoxNzQ4MDMxOTc2fQ.rqXfZpdByXtFy0gGKQrvL7KYyeqgzt2Uljn6emdwAIQ

Links:
- SQL Queries with JPA Query Methods: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

calendiary-backend/
├── src/
│   ├── main/
│   │   ├── java/com/calendiary/
│   │   │   ├── config/       # Security, Swagger, etc.
│   │   │   ├── controller/   # API endpoints
│   │   │   ├── dto/          # Request/Response objects
│   │   │   ├── model/        # JPA Entities
│   │   │   ├── repository/   # JPA Repositories
│   │   │   ├── service/      # Business logic
│   │   │   └── CalendiaryApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-dev.yml (optional)
│   └── test/                 # Unit tests
├── pom.xml                   # Maven dependencies
└── README.md