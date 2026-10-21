
# RecipeBook

A small full-stack web app for saving recipes. Each recipe has a name, an author, a description and a list of ingredients (name, amount, unit). Recipes are stored in PostgreSQL and can be viewed, added and deleted from the browser.

The project is split into three parts that run together with one command through Docker Compose:

| Part | Technology | Port |
|------|------------|------|
| Frontend | Next.js 16, React 19 (App Router) | 3000 |
| Backend | Java 21, Spring Boot 4.1, Spring Data JPA, Liquibase | 8080 |
| Database | PostgreSQL 16 | 5432 |

## Features

- Browse all saved recipes as cards (name, author, description, creation date, ingredients).
- Add a recipe with up to 10 ingredients; every ingredient has a name, an amount and a unit.
- Delete a recipe (its ingredients are deleted with it).
- Validation on both sides: in the browser before sending, and in the backend (Bean Validation).
- Database schema is versioned with Liquibase and applied automatically on startup.
- CI pipeline on GitHub Actions: unit tests, then a full Docker build and smoke checks.
- User registration and login with secure password hashing.
- Multi-Factor Authentication (MFA) for improved account security.
- Post-quantum cryptography using ML-KEM for MFA secret protection.

## How it works

```text
Browser (Next.js)  ──HTTP/JSON──▶  Spring Boot REST API  ──JPA──▶  PostgreSQL
  /view, /add                       Controller → Service → Repository     recipes, ingredients
```

1. The user opens the site; `/` redirects to `/view`.
2. The frontend calls the backend at `NEXT_PUBLIC_API_URL` (`GET /recipes`, `POST /recipes`, `DELETE /recipes/{id}`).
3. The backend `Controller` receives the request and passes it to the `Service`.
4. The `Service` converts DTOs to entities (and back) and uses the `Repository` (Spring Data JPA) to read and write the database.
5. Liquibase creates the tables on the first start; Hibernate only validates them (`ddl-auto: validate`).

## Quick start (Docker)

Requirements: Docker with Docker Compose.

```bash
git clone https://github.com/angelinazhuma/Recipe_book.git
cd Recipe_book
docker compose up --build
```

Then open:

- App: http://localhost:3000
- API: http://localhost:8080/recipes

Stop everything with `docker compose down`. Add `-v` to also delete the database volume (`postgres_data`).

## Running without Docker (development)

Requirements: JDK 21, Maven 3.9+, Node.js 20+, and Docker (only for the database).

**1. Start the database**

```bash
docker compose up -d db
```

It creates the database `recipe_book` on `localhost:5432` (user `postgres`, password `postgres`), which matches `backend/demo/src/main/resources/application.yml`.

**2. Start the backend**

```bash
cd backend/demo
mvn spring-boot:run
```

The API is available at http://localhost:8080.

**3. Start the frontend**

```bash
cd frontend
npm install
NEXT_PUBLIC_API_URL=http://localhost:8080 npm run dev
```

Open http://localhost:3000.

## API

Base path: `/recipes`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/recipes` | List all recipes |
| GET | `/recipes/{id}` | Get one recipe by id |
| POST | `/recipes` | Create a recipe |
| DELETE | `/recipes/{id}` | Delete a recipe and its ingredients |

### User Authentication

- User registration and login.
- Secure password storage using password hashing.
- Password verification during authentication.
- Multi-Factor Authentication (MFA) for additional account security.

### Multi-Factor Authentication (MFA)

RecipeBook supports Multi-Factor Authentication to provide
an additional layer of security beyond passwords.

- MFA adds a second authentication factor.
- MFA secrets are protected using cryptographic mechanisms.
- MFA helps reduce the risk of unauthorized account access
  even if a password is compromised.

### Post-Quantum Cryptography (ML-KEM)

The project uses ML-KEM (Module-Lattice-Based Key-Encapsulation
Mechanism), a post-quantum cryptographic algorithm standardized
by NIST (FIPS 203).

ML-KEM is used as part of the cryptographic mechanism for
protecting MFA secrets.

- Designed to resist attacks from quantum computers.
- Provides a mechanism for establishing shared secret keys.
- Adds post-quantum cryptographic protection to the
  MFA secret management process.

Unlike password hashing, ML-KEM is a key-encapsulation
mechanism, not a hashing or password-encryption algorithm.

## Configuration

| Variable | Used by | Default | Meaning |
|----------|---------|---------|---------|
| `NEXT_PUBLIC_API_URL` | frontend | `http://172.17.222.129:8080` (fallback in `shared/api/api.js`) | Backend URL the browser calls. Next.js reads `NEXT_PUBLIC_*` variables at **build time**, so set it before `npm run build` / `docker build`. |
| `SPRING_DATASOURCE_URL` | backend | `jdbc:postgresql://localhost:5432/recipe_book` | Database JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | backend | `postgres` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | backend | `postgres` | Database password |

The backend allows cross-origin requests only from `http://localhost:3000` and `http://172.17.222.129:3000` (see `@CrossOrigin` in `Controller.java`). If you open the frontend from another address, add it there.

The database credentials in this repository are for local development only. Do not use them in production.


## Roadmap

- Add controller tests (MockMvc) and error handling with proper 404/400 responses.
- Edit existing recipes.
- Authentication: a draft migration for a `users` table exists, but it is not included in the master changelog yet and no login is implemented.
