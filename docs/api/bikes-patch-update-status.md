# PATCH `/v1/api/bikes/{id}/status` — Update bike status

Updates only the **status** of a bike owned by the authenticated user.

This endpoint is intentionally narrow: it does **not** modify other bike fields.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Path parameters

| Name | Type | Required | Description                                  |
|------|------|----------|----------------------------------------------|
| `id` | UUID | yes      | Bike identifier whose status will be updated |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateBikeStatusRequest`)

The request body contains only one field:

| Field    | Type   | Required | Validation                                   | Description     |
|----------|--------|----------|----------------------------------------------|-----------------|
| `status` | string | yes      | `@NotNull` (`{bike.update.status.required}`) | New status code |

Notes:

- If `status` is missing or `null`, bean validation returns **400**.
- Status value validity (e.g., allowed set) is enforced in the application layer (`UpdateBikeStatusUseCase`).

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `UpdateBikeResponse`.

`UpdateBikeResponse` fields:

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "name": "My bike",
    "type": "Road",
    "status": "ACTIVE",
    "isPublic": true,
    "isExternalSync": false,
    "brand": "Brand",
    "model": "Model",
    "year": 2020,
    "serialNumber": null,
    "notes": null,
    "odometerKm": 0,
    "usageTimeMinutes": 0,
    "components": []
  },
  "message": "Success",
  "errorCode": 0
}
```

> `type` in the response is a localized label produced by `BikeApiMapper` via `MessageSource`.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`) (e.g., missing `status`).
- The use case rejects the status (domain/business validation) and throws a `BadRequestException` or `BusinessException`.

Example validation error:

```json
{
  "message": "Validation failed: status: Status is required",
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

### Update status to ACTIVE

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/status
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "status": "ACTIVE"
}
```

### Missing status (validation error)

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/status
Content-Type: application/json
Authorization: Bearer <jwt>

{}
```

Result: **400 Bad Request**.
