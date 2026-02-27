# PATCH `/v1/api/bikes/{id}` — Partially update a bike

Partially updates a bike owned by the authenticated user. Only the fields present in the JSON body are considered; missing fields are not modified.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (see error responses below).

---

## Request

### Path parameters

| Name | Type | Required | Description               |
|------|------|----------|---------------------------|
| `id` | UUID | yes      | Bike identifier to update |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateBikeRequest`)

**PATCH semantics** (important):

- **Field missing** → the field is **not** modified.
- **Field present with value** → the value is validated and applied.
- **Field present but blank string** (`""` or whitespace-only) → **400** validation error.
- **Field present with explicit `null`** → **400** (rejected by Jackson via `@JsonSetter(... Nulls.FAIL)`).

> `ownerId` cannot be updated via this endpoint.

#### Fields

| Field              | Type     | Optional | Validation                                  | Description                                                                    |
|--------------------|----------|----------|---------------------------------------------|--------------------------------------------------------------------------------|
| `name`             | string   | yes      | min length 1 (`{bike.update.name.blank}`)   | Bike name                                                                      |
| `brand`            | string   | yes      | min length 1 (`{bike.update.brand.blank}`)  | Brand                                                                          |
| `model`            | string   | yes      | min length 1 (`{bike.update.model.blank}`)  | Model                                                                          |
| `year`             | integer  | yes      | `>= 1900` (`{bike.update.year.invalid}`)    | Model year                                                                     |
| `type`             | string   | yes      | min length 1 (`{bike.update.type.blank}`)   | Bike type code (validated in use case)                                         |
| `serialNumber`     | string   | yes      | min length 1 (`{bike.update.serial.blank}`) | Serial number (if changed, uniqueness may be checked)                          |
| `notes`            | string   | yes      | min length 1 (`{bike.update.notes.blank}`)  | Notes                                                                          |
| `odometerKm`       | integer  | yes      | `>= 0` (`{bike.update.odometer.invalid}`)   | Odometer in km                                                                 |
| `usageTimeMinutes` | integer  | yes      | `>= 0` (`{bike.update.usage.invalid}`)      | Usage time in minutes                                                          |
| `isPublic`         | boolean  | yes      | (no bean validation)                        | Public visibility (`Boolean` so it can be omitted in PATCH)                    |
| `isExternalSync`   | boolean  | yes      | (no bean validation)                        | External sync flag (`Boolean` so it can be omitted in PATCH)                   |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `BikeResponse`.

---

### 400 Bad Request

Returned when:

- Authentication is missing (mapped from `ForbiddenAccessException`) — **errorCode=401**.
- Validation fails (bean validation or use case validation).
- Any field is explicitly provided as `null`.
- Blank string is provided for `name`, `brand`, `model`, `type`, `serialNumber`, or `notes`.

---

### 404 Not Found

Returned when the bike doesn’t exist (for this user) or the user isn’t the owner.

---

## Examples

### Update only the name

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "My updated bike"
}
```

### Update multiple attributes

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "brand": "Specialized",
  "model": "Allez",
  "year": 2021,
  "odometerKm": 1200,
  "usageTimeMinutes": 5400,
  "isPublic": true
}
```

### Explicit null is rejected

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": null
}
```

Result: **400 Bad Request**.
