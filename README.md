# NeoUrl - URL Shortener Backend

<div align="center">

![NeoUrl Logo](https://img.shields.io/badge/NeoUrl-URL%20Shortener-blue?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)

**A robust, scalable URL shortener backend built with Spring Boot**

[Frontend Repository](https://github.com/Jose-Daniel-Lopez/tiny-url-frontend) • [API Documentation](#api-endpoints) • [Contributing](#contributing)

</div>

## 🚀 Features

- **⚡ High Performance**: Optimized with caching and efficient database queries
- **🔒 Secure**: Authentication-protected URL creation with configurable auth codes
- **📊 Analytics**: Built-in click tracking and access statistics
- **🔄 RESTful API**: Clean, well-documented REST endpoints
- **🗄️ MongoDB Integration**: NoSQL database for scalable data storage
- **⚙️ Configurable**: Environment-based configuration for different deployments
- **📈 Monitoring**: Spring Boot Actuator for health checks and metrics
- **🔧 Developer-Friendly**: Comprehensive logging and error handling

### Core Functionality

- **URL Shortening**: Create short URLs with custom aliases or auto-generated codes
- **URL Redirection**: Fast redirects with caching for optimal performance
- **URL Management**: CRUD operations for URL entities
- **Analytics Tracking**: Click count and access statistics
- **Alias Validation**: Unique alias enforcement and collision prevention
- **CORS Support**: Configurable cross-origin resource sharing
- **Input Validation**: Comprehensive request validation with Jakarta Validation

## 🛠️ Tech Stack

| Technology              | Purpose              | Version |
| ----------------------- | -------------------- | ------- |
| **Spring Boot**         | Framework            | 3.5.5   |
| **Java**                | Programming Language | 23      |
| **MongoDB**             | Database             | Latest  |
| **Spring Data MongoDB** | Data Access          | Latest  |
| **Spring Cache**        | Caching              | Latest  |
| **Spring Validation**   | Input Validation     | Latest  |
| **Spring Actuator**     | Monitoring           | Latest  |
| **Lombok**              | Code Generation      | Latest  |
| **Maven**               | Build Tool           | Latest  |

### Additional Tools

- **Spring AI** - AI-powered features integration
- **MongoDB Atlas** - Cloud database service
- **Jakarta Validation** - Bean validation
- **Spring Boot DevTools** - Development tools

## 📦 Installation

### Prerequisites

- Java 23 or higher
- Maven 3.6+ or higher
- MongoDB Atlas account (or local MongoDB instance)

### Setup

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd tiny-url-backend
   ```

2. **Environment Configuration**

   ```bash
   cp .env.example .env
   ```

   Configure your environment variables:

   ```env
   MONGO_URI=mongodb+srv://username:password@cluster.mongodb.net/tinyurl
   SERVER_PORT=8080
   BASE_URL=https://your-domain.com
   AUTH_CODE=your-secure-auth-code
   ```

3. **Build the project**

   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── tinyurl/
│   │           ├── TinyUrlApplication.java          # Main application class
│   │           ├── config/                          # Configuration classes
│   │           │   ├── CacheConfig.java            # Cache configuration
│   │           │   ├── CorsConfig.java             # CORS configuration
│   │           │   ├── MongoConfig.java            # MongoDB configuration
│   │           │   └── UrlShortenerConfig.java     # URL shortener settings
│   │           ├── controller/                      # REST controllers
│   │           │   └── UrlShortenerController.java # Main API controller
│   │           ├── DTO/                            # Data Transfer Objects
│   │           │   ├── ShortenUrlRequest.java      # Request DTO
│   │           │   ├── ShortenUrlResponse.java     # Response DTO
│   │           │   ├── UrlInfoResponse.java        # Info response DTO
│   │           │   └── UrlListResponse.java        # List response DTO
│   │           ├── entity/                         # MongoDB entities
│   │           │   └── UrlEntity.java              # URL entity
│   │           ├── repository/                     # Data access layer
│   │           │   └── UrlRepository.java          # URL repository
│   │           ├── service/                        # Business logic layer
│   │           │   ├── Base62EncodingService.java  # Encoding service
│   │           │   ├── EntityNotFoundException.java # Custom exception
│   │           │   └── UrlShortenerService.java    # Main service
│   │           └── utils/                          # Utility classes
│   │               ├── ErrorResponse.java          # Error response
│   │               └── GlobalExceptionHandler.java # Exception handler
│   └── resources/
│       ├── application.properties                  # Application config
│       └── application.properties.backup           # Backup config
└── test/
    └── java/
        └── com/
            └── tinyurl/
                └── TinyUrlApplicationTests.java    # Unit tests
```

## 🚀 Available Scripts

| Command                          | Description                 |
| -------------------------------- | --------------------------- |
| `./mvnw clean`                   | Clean build artifacts       |
| `./mvnw compile`                 | Compile source code         |
| `./mvnw test`                    | Run unit tests              |
| `./mvnw package`                 | Package application JAR     |
| `./mvnw install`                 | Install to local repository |
| `./mvnw spring-boot:run`         | Run the application         |
| `./mvnw spring-boot:build-image` | Build Docker image          |

## ⚙️ Configuration

### Application Properties

The application uses `application.properties` for configuration:

```properties
# MongoDB Atlas connection
spring.data.mongodb.uri=${MONGO_URI}

# Server configuration
server.port=${SERVER_PORT}

# Cache configuration
spring.cache.type=simple

# Custom configuration
url-shortener.allowed-characters=0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
url-shortener.key-length=7
url-shortener.base-url=${BASE_URL}
tinyurl.auth-code=${AUTH_CODE}
```

### Environment Variables

| Variable      | Description                         | Required |
| ------------- | ----------------------------------- | -------- |
| `MONGO_URI`   | MongoDB connection string           | Yes      |
| `SERVER_PORT` | Server port (default: 8080)         | No       |
| `BASE_URL`    | Base URL for shortened links        | Yes      |
| `AUTH_CODE`   | Authorization code for URL creation | Yes      |

### Cache Configuration

The application uses Spring's simple cache for URL lookups:

- **Cache Name**: `urls`
- **TTL**: No expiration (persistent cache)
- **Strategy**: Synchronized access to prevent cache stampede

## 🌐 API Endpoints

### Base URL

```
http://localhost:8080/api
```

### Endpoints

| Method   | Endpoint            | Description                |
| -------- | ------------------- | -------------------------- |
| `POST`   | `/shorten`          | Create a new shortened URL |
| `GET`    | `/urls`             | Retrieve all URLs          |
| `GET`    | `/urls/{shortCode}` | Get URL information        |
| `DELETE` | `/urls/{shortCode}` | Delete a URL               |
| `GET`    | `/{shortCode}`      | Redirect to original URL   |

### Request/Response Examples

#### Create Short URL

```bash
POST /api/shorten
Content-Type: application/json

{
  "originalUrl": "https://example.com",
  "alias": "example",
  "authCode": "your-auth-code"
}
```

Response:

```json
{
  "shortUrl": "https://your-domain.com/example"
}
```

#### Get All URLs

```bash
GET /api/urls
```

Response:

```json
[
  {
    "shortUrl": "https://your-domain.com/example",
    "originalUrl": "https://example.com",
    "alias": "example",
    "createdDate": "2024-01-01T00:00:00Z",
    "clickCount": 42
  }
]
```

#### Redirect

```bash
GET /example
```

Returns HTTP 302 redirect to original URL.

## 🗄️ Database Schema

### URL Entity

```javascript
{
  _id: ObjectId,
  originalUrl: String,     // The original long URL
  alias: String,          // Unique short code/alias
  createdDate: Date,      // Auto-generated creation timestamp
  clickCount: Number,     // Access counter
  version: Number         // Optimistic locking version
}
```

### Indexes

- **Unique Index**: `alias` field for fast lookups
- **Compound Index**: `createdDate` for analytics queries

## 🚀 Deployment

### Production Build

```bash
./mvnw clean package -DskipTests
```

### Docker Deployment

```dockerfile
FROM openjdk:23-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Environment Setup

Ensure the following environment variables are set in production:

```env
MONGO_URI=mongodb+srv://prod-user:password@prod-cluster.mongodb.net/tinyurl
SERVER_PORT=8080
BASE_URL=https://your-production-domain.com
AUTH_CODE=secure-production-auth-code
```

### Health Checks

The application provides health endpoints via Spring Boot Actuator:

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

## 🧪 Development

### Running Tests

```bash
./mvnw test
```

### Code Quality

The project follows Spring Boot best practices:

- **Layered Architecture**: Clear separation of concerns
- **Dependency Injection**: Constructor injection pattern
- **Exception Handling**: Global exception handler
- **Validation**: Jakarta Bean Validation
- **Documentation**: Comprehensive JavaDoc comments

### Development Tools

- **Spring Boot DevTools**: Automatic restart on changes
- **Lombok**: Reduces boilerplate code
- **Maven Wrapper**: Consistent build environment

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Development Guidelines

- Follow Spring Boot coding conventions
- Write comprehensive unit tests
- Update documentation for API changes
- Use meaningful commit messages
- Maintain backward compatibility

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙋‍♂️ Support

For support, please open an issue in the GitHub repository or contact the development team.

---

<div align="center">

**Built with ❤️ using Spring Boot and modern Java technologies**

</div></content>
