# PATCH `/v1/api/bikes/{id}` — Partially update a bike

Partially updates a bike owned by the authenticated user. Only the fields present in the JSON body are considered; missing fields are not modified.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (see error responses below).

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes | Bike identifier to update |

### Headers

| Header | Required | Example |
|--------|----------|---------|
| `Content-Type` | yes | `application/json` |
| `Authorization` | yes | `Bearer <jwt>` |

### Body (`UpdateBikeRequest`)

**PATCH semantics** (important):

- **Field missing** → the field is **not** modified.
- **Field present with value** → the value is validated and applied.
- **Field present but blank string** (`""` or whitespace-only) → **400** validation error.
- **Field present with explicit `null`** → **400** (rejected by Jackson via `@JsonSetter(nulls = Nulls.FAIL)`).

> Note: `ownerId` cannot be updated via this endpoint.

#### Fields

| Field | Type | Optional | Validation | Description |
|------|------|----------|------------|-------------|
| `name` | string | yes | min length 1 (`{bike.update.name.blank}`) | Bike name |
| `brand` | string | yes | min length 1 (`{bike.update.brand.blank}`) | Brand |
| `model` | string | yes | min length 1 (`{bike.update.model.blank}`) | Model |
| `year` | integer | yes | `>= 1900` (`{bike.update.year.invalid}`) | Model year |
| `type` | string | yes | min length 1 (`{bike.update.type.blank}`) | Bike type code (validated in use case; invalid → `{bike.update.type.invalid}`) |
| `serialNumber` | string | yes | min length 1 (`{bike.update.serial.blank}`) | Serial number (if changed, uniqueness is checked) |
| `notes` | string | yes | min length 1 (`{bike.update.notes.blank}`) | Notes |
| `odometerKm` | integer | yes | `>= 0` (`{bike.update.odometer.invalid}`) | Odometer in km |
| `usageTimeMinutes` | integer | yes | `>= 0` (`{bike.update.usage.invalid}`) | Usage time in minutes |
| `isPublic` | boolean | yes | (no bean validation) | Public visibility |
| `isExternalSync` | boolean | yes | (no bean validation) | External sync flag |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `UpdateBikeResponse`.

#### Body (`UpdateBikeResponse`)

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "name": "New name",
    "type": "ROAD",
    "isPublic": true,
    "isExternalSync": false,
    "brand": "Brand",
    "model": "Model",
    "year": 2020,
    "serialNumber": null,
    "notes": null,
    "odometerKm": 0,
    "usageTimeMinutes": 0
  },
  "message": "Success",
  "errorCode": 0
}
```

> The exact `ApiResponse` wrapper fields depend on your `ApiResponse` implementation; the important part is the `data` shape above.

---

### 400 Bad Request

Returned when:

- Authentication is missing (mapped from `ForbiddenAccessException`) — **errorCode=401**.
- Validation fails (bean validation or use case validation).
- Any field is explicitly provided as `null`.
- Blank string is provided for `name`, `brand`, `model`, `type`, `serialNumber`, or `notes`.

Example validation error (shape depends on `ApiResponse`):

```json
{
  "message": "Validation failed: name: Name cannot be blank",
  "errorCode": 400
}
```

Example auth-required error:

```json
{
  "message": "Authentication is required.",
  "errorCode": 401
}
```

---

### 404 Not Found

Returned when the bike doesn’t exist (for this user) or the user isn’t the owner.

Example:

```json
{
  "message": "Bike not found.",
  "errorCode": 404
}
```

---

## Examples

### Update only the name

**Request**

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

