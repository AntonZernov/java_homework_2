# Content Management API

REST API для управления статьями, видео, подкастами и эпизодами подкастов на Spring Boot.

## Системные требования

- **Java**: 21
- **Maven**: 3.8+
- **Docker и Docker Compose**: 20
- **PostgreSQL**: 17 (включено через Docker Compose)

## Возможности

- RESTful API для CRUD-операций со статьями, видео, подкастами и эпизодами подкастов
- Swagger UI для документации API
- Flyway-миграции базы данных
- Валидация входных данных
- Готовая конфигурация Docker Compose с PostgreSQL

## API Endpoints

### Статьи

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `/articles` | Поиск/список статей |
| GET | `/articles/{id}` | Получить статью по ID |
| POST | `/articles` | Создать новую статью |
| PATCH | `/articles/{id}` | Обновить статью |
| DELETE | `/articles/{id}` | Удалить статью |

### Видео

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `/videos` | Поиск/список видео |
| GET | `/videos/{id}` | Получить видео по ID |
| POST | `/videos` | Создать новое видео |
| PATCH | `/videos/{id}` | Обновить видео |
| DELETE | `/videos/{id}` | Удалить видео |

### Подкасты

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `/podcasts` | Поиск/список подкастов |
| GET | `/podcasts/{id}` | Получить подкаст по ID |
| POST | `/podcasts` | Создать новый подкаст |
| PATCH | `/podcasts/{id}` | Обновить подкаст |
| DELETE | `/podcasts/{id}` | Удалить подкаст |
| GET | `/podcasts/{podcastId}/episodes` | Получить эпизоды подкаста |
| POST | `/podcasts/{podcastId}/episodes` | Добавить эпизод в подкаст |

### Эпизоды подкастов

| Метод | Endpoint | Описание |
|-------|----------|----------|
| GET | `/episodes/{episodeId}` | Получить эпизод по ID |
| PATCH | `/episodes/{episodeId}` | Обновить эпизод |
| DELETE | `/episodes/{episodeId}` | Удалить эпизод |

## Запуск локально

### Запуск приложения одной командой

```bash
docker compose up --build
```

Запуск одной командой может выполняться длятельное время из-за создания maven cache в образе докера.
Для ускорения процесса можно вручную выполнить сборку:
```bash
 mvn clean package
 ```
заменить содержимое Dockerfile на:
```commandline
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```
и запустить docker compose.
```bash
docker compose up --build
```
Будут запущены:
- **PostgreSQL 17** на порту `5433`
- **Spring Boot приложение** на порту `8080`

### Доступ к приложению

- Приложение: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- PostgreSQL: `localhost:5433`
### Остановка сервисов

```bash
docker-compose down
```

Для удаления томов и данных:

```bash
docker-compose down -v
```

## Сборка

### Сборка JAR

```bash
mvn clean package
```

### Запуск тестов

```bash
mvn test
```

### Запуск JAR

```bash
java -jar target/java-homework-2-0.0.1-SNAPSHOT.jar
```

## Конфигурация

Конфигурация базы данных находится в `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://postgres-db:5432/content
spring.datasource.username=admin
spring.datasource.password=1111
```
Для удобства тестирования в БД создан стартовый набор данных.
