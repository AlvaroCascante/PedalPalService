# POST `/v1/api/bikes` — Create a bike

Creates a new bike for the authenticated user.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Headers

| Header | Required | Example |
|--------|----------|---------|
| `Content-Type` | yes | `application/json` |
| `Authorization` | yes | `Bearer <jwt>` |

### Body (`CreateBikeRequest`)

| Field | Type | Required | Validation | Description |
|------|------|----------|------------|-------------|
| `name` | string | yes | `@NotBlank` (`{bike.create.name.blank}`) | Bike name |
| `isPublic` | boolean | no | — | Public visibility |
| `type` | string | yes | `@NotNull` (`{bike.create.type.required}`) | Bike type (string code) |
| `brand` | string | no | max 100 (`{bike.create.brand.max}`) | Brand |
| `model` | string | no | max 100 (`{bike.create.model.max}`) | Model |
| `year` | integer | no | `>= 1900` (`{bike.create.year.invalid}`) | Model year |
| `serialNumber` | string | no | max 100 (`{bike.create.serial.max}`) | Serial number (must be unique if provided) |
| `notes` | string | no | max 1000 (`{bike.create.notes.max}`) | Notes |
| `odometerKm` | integer | no | `>= 0` (`{bike.create.odometer.invalid}`) | Odometer in km |
| `usageTimeMinutes` | integer | no | `>= 0` (`{bike.create.usage.invalid}`) | Usage time in minutes |
| `isExternalSync` | boolean | no | — | External sync flag |

---

## Responses

### 201 Created

Returns an `ApiResponse` whose `data` is a `CreateBikeResponse`.

- `Location` header is set to `/api/bikes/{id}` (note: current controller uses `/api/bikes/` not `/v1/api/bikes/`).

#### Body (`CreateBikeResponse`)

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "name": "My bike",
    "type": "ROAD",
    "isPublic": false,
    "isExternalSync": false,
    "brand": "Canyon",
    "model": "Ultimate",
    "year": 2022,
    "serialNumber": "SN-123",
    "notes": "Some notes",
    "odometerKm": 1234,
    "usageTimeMinutes": 5678
  },
  "message": "Success",
  "errorCode": 0
}
```

> The exact `ApiResponse` wrapper fields depend on your `ApiResponse` implementation; the important part is the `data` shape above.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).
- A domain/business rule fails (e.g., serial number already exists). This is thrown by the use case as `BusinessException`.

Example validation error:

```json
{
  "message": "Validation failed: name: Bike name is required",
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

Example business rule error (serial already exists):

```json
{
  "message": "A bike with the same serial number already exists: SN-123",
  "errorCode": 400
}
```

---

## Examples

### Minimal create

```http
POST /v1/api/bikes
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "My bike",
  "type": "ROAD"
}
```

### Full create

```http
POST /v1/api/bikes
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "Canyon Ultimate",
  "isPublic": true,
  "type": "ROAD",
  "brand": "Canyon",
  "model": "Ultimate",
  "year": 2022,
  "serialNumber": "SN-123",
  "notes": "Some notes",
  "odometerKm": 1234,
  "usageTimeMinutes": 5678,
  "isExternalSync": true
}
```

### Validation example (blank name)

```http
POST /v1/api/bikes
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": " ",
  "type": "ROAD"
}
```

Result: **400 Bad Request**.

