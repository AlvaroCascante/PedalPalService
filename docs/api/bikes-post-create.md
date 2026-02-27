# POST `/v1/api/bikes` — Create a bike

Creates a new bike for the authenticated user.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`CreateBikeRequest`)

| Field              | Type    | Required | Validation                                 | Description                                |
|--------------------|---------|----------|--------------------------------------------|--------------------------------------------|
| `name`             | string  | yes      | `@NotBlank` (`{bike.create.name.blank}`)   | Bike name                                  |
| `isPublic`         | boolean | yes      | —                                          | Public visibility                          |
| `type`             | string  | yes      | `@NotNull` (`{bike.create.type.required}`) | Bike type code (string)                    |
| `brand`            | string  | no       | max 100 (`{bike.create.brand.max}`)        | Brand                                      |
| `model`            | string  | no       | max 100 (`{bike.create.model.max}`)        | Model                                      |
| `year`             | integer | no       | `>= 1900` (`{bike.create.year.invalid}`)   | Model year                                 |
| `serialNumber`     | string  | no       | max 100 (`{bike.create.serial.max}`)       | Serial number (must be unique if provided) |
| `notes`            | string  | no       | max 1000 (`{bike.create.notes.max}`)       | Notes                                      |
| `odometerKm`       | integer | no       | `>= 0` (`{bike.create.odometer.invalid}`)  | Odometer in km                             |
| `usageTimeMinutes` | integer | no       | `>= 0` (`{bike.create.usage.invalid}`)     | Usage time in minutes                      |
| `isExternalSync`   | boolean | yes      | —                                          | External sync flag                         |

---

## Responses

### 201 Created

Returns an `ApiResponse` whose `data` is a `BikeResponse`.

- `Location` header is set to `/api/bikes/{id}`.

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
    "brand": "Canyon",
    "model": "Ultimate",
    "year": 2022,
    "serialNumber": "SN-123",
    "notes": "Some notes",
    "odometerKm": 1234,
    "usageTimeMinutes": 5678,
    "components": []
  },
  "message": "Success",
  "errorCode": 0
}
```

> `type` in the response is a localized label produced by `BikeApiMapper` via `MessageSource`. On creation, `components` is typically an empty array.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).
- A domain/business rule fails (e.g., serial number already exists). This is thrown by the use case as `BusinessException`.

---

## Examples

### Minimal create

```http
POST /v1/api/bikes
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "My bike",
  "isPublic": false,
  "type": "ROAD",
  "isExternalSync": false
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
  "isPublic": false,
  "type": "ROAD",
  "isExternalSync": false
}
```

Result: **400 Bad Request**.
