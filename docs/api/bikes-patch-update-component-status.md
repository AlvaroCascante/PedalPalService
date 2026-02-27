# PATCH `/v1/api/bikes/{bikeId}/components/{componentId}/status` — Update bike component status

Updates only the **status** of a single bike component that belongs to a bike owned by the authenticated user.

This endpoint is intentionally narrow: it does **not** modify other component fields.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

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

### Body (`UpdateBikeComponentStatusRequest`)

| Field    | Type   | Required | Validation                                             | Description                                           |
|----------|--------|----------|--------------------------------------------------------|-------------------------------------------------------|
| `status` | string | yes      | `@NotNull` (`{bike.component.update.status.blank}`)    | New component status code (e.g. `ACTIVE`, `INACTIVE`) |

Notes:

- If `status` is missing or `null`, bean validation returns **400**.
- Status value validity (allowed set / unknown handling) is enforced in the application layer (`UpdateBikeComponentStatusUseCase`).

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `BikeResponse` representing the bike after updating the component status.

`BikeResponse` includes a `components` array.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`) (e.g. missing `status`).
- The use case rejects the status and throws a `BadRequestException` or `BusinessException`.

---

### 404 Not Found

Returned when:

- The bike doesn’t exist for this user (use case throws `RecordNotFoundException("bike.not.found")`).
- The component is not found on that bike (use case throws `RecordNotFoundException("bike.component.not.found")`).

---

## Examples

### Update status to INACTIVE

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612/status
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "status": "INACTIVE"
}
```

### Missing status

```http
PATCH /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612/status
Content-Type: application/json
Authorization: Bearer <jwt>

{}
```

Result: **400 Bad Request**.
