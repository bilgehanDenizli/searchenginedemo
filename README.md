# Pet Clinic Search Demo

A small Spring Boot REST API for pet clinic data. PostgreSQL is the source of truth and Elasticsearch powers search; both are configured for local development through Docker Compose.

## Run

The local PostgreSQL connection defaults are `jdbc:postgresql://localhost:5432/petclinic`, username `petclinic`, and password `petclinic`; they can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

```powershell
docker compose up -d postgres elasticsearch
.\mvnw spring-boot:run
```

Elasticsearch is available in `compose.yaml` and the app connects to `http://localhost:9200` by default. Start it before starting the app; Spring Data checks the `pets` index during startup.

For a different Elasticsearch URL, set `ELASTICSEARCH_URI`, for example `http://localhost:9201`.

## Sample data and bulk indexing

[`src/main/resources/db/sample-data.sql`](src/main/resources/db/sample-data.sql) seeds related owners, pets, and visits. It contains 10 hand-written owners, 20 pets, and 20 visits, plus deterministic generated data (100 owners, 200 pets, and 500 visits). It uses explicit IDs and `ON CONFLICT DO NOTHING`, so it is safe to rerun and will not overwrite existing rows.

With the Compose services running, load the seed into PostgreSQL:

```powershell
docker compose cp src/main/resources/db/sample-data.sql postgres:/tmp/sample-data.sql
docker compose exec -T postgres psql -v ON_ERROR_STOP=1 -U petclinic -d petclinic -f /tmp/sample-data.sql
```

After the application starts, call this endpoint to bulk-index all owner, pet, and visit records from PostgreSQL:

```powershell
curl.exe -X POST http://localhost:8080/api/admin/search/index
```

The response reports the number of documents upserted per index. Reindexing updates documents with matching IDs but does not remove Elasticsearch documents that no longer exist in PostgreSQL. This admin endpoint is intended for local development and should be protected before exposing the app publicly.

## REST endpoints

Each resource supports `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, and `PATCH /{id}/active`:

- `/api/owners`
- `/api/pets`
- `/api/visits`

Records are deactivated instead of deleted. For example, `PATCH /api/visits/1/active` with `{"active": false}` marks a visit inactive.

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

The relational database is the source of truth. Created or updated owners, pets, and visits are also indexed in Elasticsearch for the search layer.
