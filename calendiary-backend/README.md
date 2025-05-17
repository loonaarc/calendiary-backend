How to run:
- Make sure Docker is installed and runinng https://www.docker.com/products/docker-desktop/
- Run docker.compose.yml file
- Start Application

Make sure these Intellij Plugins are installed:
- Lombok
- Docker

Settings:
Settings > Build > Compiler > Annotation Processors
✔ Enable annotation processing

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