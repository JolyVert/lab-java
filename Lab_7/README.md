# Lab7 – Aplikacja webowa (Spring Boot + Angular / JHipster)

Projekt wykonany w języku Java w ramach laboratorium z przedmiotu Platformy Programistyczne .NET i Java.

## Opis projektu

Aplikacja webowa typu monolit wygenerowana przy użyciu generatora **JHipster 8.11.0**, łącząca reaktywny backend w Spring Boot z frontendem napisanym w Angularze.

Aplikacja zapewnia:

- pełną strukturę aplikacji webowej (backend + frontend),
- mechanizm uwierzytelniania użytkowników (JWT),
- panel administracyjny (zarządzanie kontami, metryki, logi, konfiguracja),
- gotową obsługę bazy danych MySQL,
- internacjonalizację (i18n),
- wsparcie dla testów jednostkowych (JUnit, Jest).

## Technologie

### Backend
- Java 17+
- Spring Boot (reactive / WebFlux)
- Spring Security + JWT
- Maven
- MySQL (dev / prod)
- Liquibase

### Frontend
- Angular
- TypeScript
- Bootstrap
- Webpack
- Jest

### Narzędzia
- JHipster 8.11.0
- Docker / Docker Compose
- Sonar
- ESLint, Prettier, Husky, lint-staged

## Konfiguracja JHipster

Wybrane opcje generatora (`.yo-rc.json`):

- `applicationType`: monolith
- `authenticationType`: jwt
- `clientFramework`: angular
- `databaseType`: sql (MySQL – dev i prod)
- `reactive`: true
- `withAdminUi`: true
- `enableTranslation`: true
- `packageName`: `com.mycompany.myapp`

## Struktura projektu

```text
Lab_7
├── src
│   ├── main
│   │   ├── docker          # konfiguracje Docker Compose
│   │   ├── java
│   │   │   └── com.mycompany.myapp
│   │   │       ├── aop
│   │   │       ├── config
│   │   │       ├── domain
│   │   │       ├── management
│   │   │       ├── repository
│   │   │       ├── security
│   │   │       ├── service
│   │   │       └── web
│   │   ├── resources       # pliki konfiguracyjne aplikacji
│   │   └── webapp
│   │       └── app
│   │           ├── account
│   │           ├── admin
│   │           ├── config
│   │           ├── core
│   │           ├── entities
│   │           ├── home
│   │           ├── layouts
│   │           ├── login
│   │           └── shared
│   └── test                # testy backend / frontend
├── pom.xml
├── package.json
├── angular.json
└── README.md
```

## Opis pakietów backendu

### config
Konfiguracja Spring Boot (bezpieczeństwo, baza danych, cache, web).

### domain
Encje JPA reprezentujące model danych aplikacji.

### repository
Interfejsy repozytoriów (Spring Data) do operacji na bazie danych.

### security
Komponenty odpowiedzialne za uwierzytelnianie i autoryzację (JWT, role, użytkownicy).

### service
Warstwa logiki biznesowej oraz DTO przekazywane między warstwami.

### web
Kontrolery REST udostępniające API aplikacji.

### aop / management
Aspekty (logowanie wywołań) oraz konfiguracja zarządzania aplikacją (Actuator, metryki).

## Opis modułów frontendu (Angular)

- **account** – rejestracja, logowanie, zarządzanie kontem użytkownika,
- **admin** – panel administracyjny (użytkownicy, metryki, logi, konfiguracja),
- **entities** – moduły wygenerowane na podstawie encji JDL,
- **home** / **login** / **layouts** – strony i komponenty układu aplikacji,
- **core** / **shared** – współdzielone usługi, modele i komponenty.

## Uruchomienie projektu

### Instalacja zależności frontendu

```bash
./npmw install
```

### Tryb deweloperski

W dwóch osobnych terminalach:

```bash
./mvnw
```

```bash
./npmw start
```

Aplikacja będzie dostępna pod adresem [http://localhost:8080](http://localhost:8080).

### Uruchomienie usług w Dockerze

```bash
docker compose -f src/main/docker/services.yml up -d
```

### Build produkcyjny (jar)

```bash
./mvnw -Pprod clean verify
java -jar target/*.jar
```

### Build produkcyjny (war)

```bash
./mvnw -Pprod,war clean verify
```

## Testowanie

### Testy backendu

```bash
./mvnw verify
```

### Testy frontendu

```bash
./npmw test
```

### Analiza jakości kodu (Sonar)

```bash
docker compose -f src/main/docker/sonar.yml up -d
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

## Autor

Projekt wykonany w ramach laboratorium nr 7.
