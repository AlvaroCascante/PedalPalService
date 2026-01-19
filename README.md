# PedalPal Spring Boot Project

This repository is a PedalPal Spring Boot project

## Project Information

- **Group:** `com.quetoquenana`
- **Artifact:** `pedalpal`
- **Name:** `PedalPal`
- **Description:** PedalPal Spring Boot project
- **Package name:** `com.quetoquenana.PedalPal`

## Included Dependencies

This pedalpal includes the following dependencies by default (as listed in `pom.xml`):

- **Spring Boot Actuator** (`spring-boot-starter-actuator`): Monitoring and management endpoints.
- **Spring Data JPA** (`spring-boot-starter-data-jpa`): Simplifies data access and ORM with JPA.
- **Spring Security** (`spring-boot-starter-security`): Authentication and authorization capabilities.
- **Spring Web** (`spring-boot-starter-web`): Build RESTful web services and web applications.
- **Spring Boot Test** (`spring-boot-starter-test`, test scope): Testing support for Spring Boot applications.
- **Spring HATEOAS** (`spring-boot-starter-hateoas`): Hypermedia-driven REST APIs.
- **Flyway Core** (`flyway-core`): Database schema migrations.
- **Flyway PostgreSQL Database Support** (`flyway-database-postgresql`): Flyway support for PostgreSQL.
- **Spring Boot DevTools** (`spring-boot-devtools`, runtime, optional): Rapid development and automatic restarts.
- **Spring Boot Docker Compose** (`spring-boot-docker-compose`, runtime, optional): Run dependent services with Docker Compose.
- **PostgreSQL Driver** (`postgresql`, runtime): Connects to PostgreSQL databases.
- **Lombok** (`lombok`, optional): Simplifies Java code with annotations for boilerplate reduction.
- **Spring Security Test** (`spring-security-test`, test scope): Testing support for Spring Security.

## Security Configuration

This uses Spring Security with the following configuration:

- **HTTP Basic Authentication** is enabled for all endpoints.
- **User Details:**
  - Username: `user`
  - Password: `password` (BCrypt encoded)
  - Role: ``
- **Access Rules:**
  - All endpoints: Require authentication and the `` role
- **Configuration Location:** See `src/main/java/com/quetoquenana/pedalpal/config/SecurityConfig.java` for details.

## Internationalization (i18n) Support

This project is ready for multi-language support using Spring Boot's internationalization features:

- **Message Resource Files:**
  - `src/main/resources/messages.properties` (default, English)
  - `src/main/resources/messages_es.properties` (Spanish)
- **Configuration:**
  - See `src/main/java/com/quetoquenana/pedalpal/config/I18nConfig.java` for MessageSource and LocaleResolver beans.
- **Usage:**
  - Inject `MessageSource` into your services or controllers and use `messageSource.getMessage("welcome.message", null, locale)` to retrieve localized messages.
  - The default locale is English; you can switch locales using the `SessionLocaleResolver` or by customizing locale resolution.
  - Rest controllers use the `Accept-Language` header to set the locale.
- **Adding More Languages:**
  - Create additional files like `messages_fr.properties` for French, etc.

Example usage in a service/controller:

```java
@Autowired
private MessageSource messageSource;

public String getWelcomeMessage(Locale locale) {
    return messageSource.getMessage("welcome.message", null, locale);
}
```

## Getting Started

1. **Clone this repository** and update the project information as needed.
2. **Configure your database** in the properties files (`application-dev.properties`, `application-prd.properties`).
3. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API:**


5.**Test with Postman:**
   - Import the collection from the `postman/collections` folder and run example requests.

## Customization

- Update the `pom.xml` to add or remove dependencies as needed.
- Modify the package structure and application properties to fit your requirements.
- Extend the execution tracking feature or add new features as needed.

## License

This project is provided as-is for bootstrapping new Spring Boot projects.
