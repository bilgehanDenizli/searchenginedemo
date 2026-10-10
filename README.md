# Pet Clinic Search Demo

A small Spring Boot REST API for pet clinic data. PostgreSQL is available through a profile; the default H2 in-memory database lets the application run without installing a database.

## Run

```powershell
.\mvnw spring-boot:run
```

The default H2 database is recreated when the application stops. Its console is available at `http://localhost:8080/h2-console` while the app is running. Use JDBC URL `jdbc:h2:mem:petclinic`, user `sa`, and an empty password.

To use PostgreSQL, create a database and run with the `postgres` profile. The connection defaults are `jdbc:postgresql://localhost:5432/petclinic`, username `petclinic`, and password `petclinic`; they can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/petclinic'
$env:DB_USERNAME = 'petclinic'
$env:DB_PASSWORD = 'petclinic'
.\mvnw '-Dspring-boot.run.profiles=postgres' spring-boot:run
```

Elasticsearch is available in `compose.yaml` and the app connects to `http://localhost:9200` by default. Start it before starting the app; Spring Data checks the `pets` index during startup.

```powershell
docker compose up -d elasticsearch
```

For a different Elasticsearch URL, set `ELASTICSEARCH_URI`, for example `http://localhost:9201`.

## REST endpoints

Each resource supports `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, and `DELETE /{id}`:

- `/api/owners`
- `/api/pets`
- `/api/visits`

Create an owner:

```json
{
  "firstName": "Sam",
  "lastName": "Taylor",
  "email": "sam@example.com",
  "phone": "+1-555-0100"
}
```

Create a pet using the returned owner ID:

```json
{
  "name": "Milo",
  "species": "DOG",
  "breed": "Mixed",
  "birthDate": "2022-04-10",
  "ownerId": 1
}
```

Create a visit using the returned pet ID:

```json
{
  "petId": 1,
  "scheduledAt": "2026-11-02T10:30:00",
  "reason": "Annual checkup",
  "notes": "",
  "status": "SCHEDULED"
}
```

Pet species: `DOG`, `CAT`, `BIRD`, `RABBIT`, `REPTILE`, `OTHER`. Visit status: `SCHEDULED`, `COMPLETED`, `CANCELLED`. Invalid requests return `400`, missing records `404`, and duplicate or referenced records `409`.

The relational database is the source of truth. Newly created pets are also indexed in Elasticsearch's `pets` index for the search layer.
