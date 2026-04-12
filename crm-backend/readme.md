# CRM Backend

REST API za Customer Relationship Management sistem izgrađen sa Spring Boot 3.

## Tehnologije
- Java 17
- Spring Boot 3
- Spring Security 6 + JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Docker

## Pokretanje

### Preduslovi
- Java 17+
- Docker Desktop

### Baza podataka
```bash
docker run --name crm-postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=crmdb -p 5432:5432 -d postgres:15
```

### Aplikacija
```bash
./mvnw spring-boot:run
```

API je dostupan na `http://localhost:8080`

## Korisničke uloge
| Uloga | Opis |
|-------|------|
| ADMIN | Pun pristup, može brisati |
| MANAGER | Upravlja timom |
| REP | Upravlja svojim kontaktima i dealovima |

## API Endpointi

### Auth
- `POST /api/auth/register` — registracija
- `POST /api/auth/login` — login, vraća JWT token

### Contacts
- `GET /api/contacts` — lista sa paginacijom i pretragom
- `POST /api/contacts` — kreiranje
- `PUT /api/contacts/{id}` — izmena
- `DELETE /api/contacts/{id}` — brisanje (samo ADMIN)

### Deals
- `GET /api/deals` — lista, filter po stage-u
- `POST /api/deals` — kreiranje
- `PUT /api/deals/{id}/stage` — promena stage-a
- `GET /api/dashboard/stats` — statistike

## Frontend
React frontend je dostupan na: [crm-frontend](https://github.com/TVOJE_IME/crm-frontend)