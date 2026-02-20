# PATCH `/v1/api/bikes/{bikeId}/components/{componentId}` — Partially update a bike component

Partially updates a **single component** that belongs to a bike owned by the authenticated user.

This endpoint is the component-level equivalent of the bike PATCH endpoint:

- **Only fields present** in the JSON body are considered.
- **Missing fields are not modified**.
- **Explicit null values are rejected** (via Jackson `@JsonSetter(nulls = Nulls.FAIL)`).

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Path parameters

| Name          | Type | Required | Description                             |
|---------------|------|----------|-----------------------------------------|
| `bikeId`      | UUID | yes      | Bike identifier that owns the component |
| `componentId` | UUID | yes      | Component identifier to update          |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateBikeComponentRequest`)

**PATCH semantics** (important):

- **Field missing** → the field is **not** modified.
- **Field present with value** → the value is validated and applied.
- **Field present but blank string** (`""` or whitespace-only) → **400** validation error.
- **Field present with explicit `null`** → **400** (rejected by Jackson).

#### Fields

| Field              | Type    | Optional | Validation                                                 | Description                                                                              |
|--------------------|---------|----------|------------------------------------------------------------|------------------------------------------------------------------------------------------|
| `name`             | string  | yes      | `@Size(min=1)` (`{bike.add.component.name.blank}`)         | Component name                                                                           |
| `type`             | string  | yes      | `@Size(min=1)` (`{bike.add.component.type.required}`)      | Component type code (e.g. `CHAIN`, must exist in system codes category `COMPONENT_TYPE`) |
| `brand`            | string  | yes      | `@Size(min=1,max=100)` (`{bike.add.component.brand.max}`)  | Brand                                                                                    |
| `model`            | string  | yes      | `@Size(min=1,max=100)` (`{bike.add.component.model.max}`)  | Model                                                                                    |
| `notes`            | string  | yes      | `@Size(min=1,max=1000)` (`{bike.add.component.notes.max}`) | Notes                                                                                    |
| `odometerKm`       | integer | yes      | `@Min(0)` (`{bike.add.component.odometer.invalid}`)        | Odometer in km                                                                           |
| `usageTimeMinutes` | integer | yes      | `@Min(0)` (`{bike.add.component.usage.invalid}`)           | Usage time in minutes                                                                    |

> Implementation note: the current DTO uses `int odometerKm` and `int usageTimeMinutes` (primitive), which means they default to `0` when the field is omitted. That makes them effectively **always present** from the controller’s point of view. If you want true PATCH semantics for these fields, change them to `Integer`.

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `BikeResponse` representing the bike after applying the component update.

#### Body (`BikeResponse`)

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "name": "My bike",
    "type": "Road",
    "status": "ACTIVE",
    "isPublic": false,
    "isExternalSync": false,
    "brand": "Brand",
    "model": "Model",
    "year": 2020,
    "serialNumber": null,
    "notes": null,
    "odometerKm": 0,
    "usageTimeMinutes": 0,
    "components": [
      {
        "id": "4e100290-fc14-4d1f-b1f8-9f9338702612",
        "type": "CHAIN",
        "name": "New chain",
        "brand": "Shimano",
        "model": "HG",
        "notes": "New chain",
        "odometerKm": 10,
        "usageTimeMinutes": 20
      }
    ]
  },
  "message": "Success",
  "errorCode": 0
}
```

Notes:

- `type` in the response is a localized label produced by `BikeApiMapper` via `MessageSource`.
- `components` is always present (empty array if none).

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).
- Any field is explicitly provided as `null`.

Example validation error (shape depends on your `ApiResponse`):

```json
{
  "message": "Validation failed: name: Name cannot be blank",
  "errorCode": 400
}
```

---

### 404 Not Found

Returned when:

- The bike doesn’t exist for this user (use case throws `RecordNotFoundException("bike.not.found")`).
- The bike exists but the component is not found on that bike (use case throws `RecordNotFoundException("bike.component.not.found")`).
- A provided component type code doesn’t exist (use case throws `RecordNotFoundException("bike.component.type.not.found", type)`).

Example:

```json
{
  "message": "Bike component not found.",
  "errorCode": 404
}
```

---

## Examples

### Update only the component name

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "New chain"
}
```

### Update type and mileage

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "type": "CASSETTE",
  "odometerKm": 500,
  "usageTimeMinutes": 1200
}
```

### Explicit null is rejected

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": null
}
```

Result: **400 Bad Request**.

