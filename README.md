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

## Maintenance Suggestion Job

The project includes a configurable job that can automatically generate maintenance suggestions from bikes and their components. The job is implemented as a Spring scheduled component and also supports manual triggering (for example, via a controller endpoint you can add later).

Key points:
- The job logic is in `com.quetoquenana.pedalpal.application.job.MaintenanceSuggestionJob` and it delegates to `MaintenanceSuggestionService.generateBikesSuggestions()`.
- Properties are bound to the `MaintenanceSuggestionJobProperties` class (`com.quetoquenana.pedalpal.config.MaintenanceSuggestionJobProperties`) using the prefix `jobs.maintenance-suggestions`.
- The `PedalPalApplication` class enables the properties binding with `@EnableConfigurationProperties(MaintenanceSuggestionJobProperties.class)`.

Primary configuration properties (prefix `jobs.maintenance-suggestions`):

- `enabled` (boolean): whether the scheduled job runs automatically. Default: `true` in dev config.
- `cron` (String): the cron expression that controls schedule (Spring cron format; see below). Example default: `0 0 2 * * *` (runs daily at 02:00:00).
- `page-size` (int): page size for paging bikes. Default: `100`.
- `suggestion-type-code` (String): system code used for suggestion type (default `SUGGESTION_TYPE_IA`).
- `priority-code` (String): default priority code for generated items (optional).
- `urgency-code` (String): default urgency code for generated items (optional).
- `component-type-filter` (List&lt;String&gt;): optional list of component-type codes to include; empty = include all.
- `deduplication-window-days` (int): skip bikes that already have suggestions created within this number of days. Default: `7`.
- `system-username` (String): username used to populate audit fields on auto-created suggestions (default `system`).

Example properties snippet (add to `application-dev.properties` or other profile):

```properties
jobs.maintenance-suggestions.enabled=true
jobs.maintenance-suggestions.cron=0 0 2 * * *
jobs.maintenance-suggestions.page-size=100
jobs.maintenance-suggestions.suggestion-type-code=SUGGESTION_TYPE_IA
jobs.maintenance-suggestions.priority-code=
jobs.maintenance-suggestions.urgency-code=
jobs.maintenance-suggestions.component-type-filter=
jobs.maintenance-suggestions.deduplication-window-days=7
jobs.maintenance-suggestions.system-username=system
```

Cron format (Spring's 6-field format)

Spring's cron expression uses 6 fields: `second minute hour day-of-month month day-of-week`.
- Common symbols:
  - `*` = any value
  - `,` = list separator (e.g. `MON,WED,FRI`)
  - `-` = range (e.g. `1-5`)
  - `/` = step values (e.g. `*/15`)
- Examples:
  - `0 0 2 * * *` → every day at 02:00:00
  - `0 */5 * * * *` → every 5 minutes
  - `0 30 1 * * MON-FRI` → 01:30 on weekdays

Timezone
- `@Scheduled` uses the JVM default timezone unless you configure a zone on the annotation or provide a different timezone in properties.

## License

This project is provided as-is for bootstrapping new Spring Boot projects.
