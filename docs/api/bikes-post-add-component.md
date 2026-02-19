# POST `/v1/api/bikes/{id}/components` — Add a component to a bike

Adds a new component to an existing bike owned by the authenticated user.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Path parameters

| Name | Type | Required | Description                                          |
|------|------|----------|------------------------------------------------------|
| `id` | UUID | yes      | Bike identifier to which the component will be added |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`AddBikeComponentRequest`)

| Field              | Type    | Required | Validation                                        | Description                                                        |
|--------------------|---------|----------|---------------------------------------------------|--------------------------------------------------------------------|
| `name`             | string  | yes      | `@NotNull` (`{bike.add.component.name.blank}`)    | Component name                                                     |
| `type`             | string  | yes      | `@NotNull` (`{bike.add.component.type.required}`) | Component type code (system code / enum-like string, e.g. `CHAIN`) |
| `brand`            | string  | no       | max 100 (`{bike.add.component.brand.max}`)        | Brand                                                              |
| `model`            | string  | no       | max 100 (`{bike.add.component.model.max}`)        | Model                                                              |
| `notes`            | string  | no       | max 1000 (`{bike.add.component.notes.max}`)       | Notes                                                              |
| `odometerKm`       | integer | no       | `>= 0` (`{bike.add.component.odometer.invalid}`)  | Odometer in km at time of install                                  |
| `usageTimeMinutes` | integer | no       | `>= 0` (`{bike.add.component.usage.invalid}`)     | Usage time in minutes at time of install                           |

> Note: although `name` is documented as “blank” in the message key, the annotation is `@NotNull` (not `@NotBlank`). If you want to reject whitespace-only values, change it to `@NotBlank`.

---

## Responses

### 201 Created

Returns an `ApiResponse` whose `data` is a `BikeResponse` representing the updated bike (including a `components` array).

- `Location` header is set to `/api/bikes/{id}` (note: current controller uses `/api/bikes/` not `/v1/api/bikes/`).

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
        "name": "Chain",
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
- `components` is returned as an array. When there are no components, it will be `[]`.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).

Example validation error:

```json
{
  "message": "Validation failed: name: Component name is required",
  "errorCode": 400
}
```

---

### 404 Not Found

Returned when:

- The bike doesn’t exist for this user (use case throws `RecordNotFoundException("bike.not.found")`).
- The component type code doesn’t exist (use case throws `RecordNotFoundException("bike.component.type.not.found", type)`).

Example:

```json
{
  "message": "Bike not found.",
  "errorCode": 404
}
```

---

## Examples

### Minimal add component

```http
POST /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "Chain",
  "type": "CHAIN"
}
```

### Full add component

```http
POST /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "Chain",
  "type": "CHAIN",
  "brand": "Shimano",
  "model": "HG",
  "notes": "New chain",
  "odometerKm": 10,
  "usageTimeMinutes": 20
}
```

