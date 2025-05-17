How to run:
- Make sure Docker is installed and runinng https://www.docker.com/products/docker-desktop/
- Make sure Intellij's Docker Plugin is installed
- Run docker.compose.yml file
- Start Application



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