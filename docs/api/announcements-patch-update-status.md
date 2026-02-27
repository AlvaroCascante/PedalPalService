# PATCH `/v1/api/announcements/{id}/status` — Update announcement status

Updates only the **status** of an existing announcement.

---

## Security

- Requires authentication.
- Requires role: `ADMIN`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

---

## Request

### Path parameters

| Name | Type | Required | Description             |
|------|------|----------|-------------------------|
| `id` | UUID | yes      | Announcement identifier |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateAnnouncementStatusRequest`)

| Field    | Type   | Required | Validation / Behavior                                                 | Description                                  |
|----------|--------|----------|-----------------------------------------------------------------------|----------------------------------------------|
| `status` | string | yes      | If blank or missing → **400** (`announcement.update.status.required`) | New status code (e.g., `ACTIVE`, `INACTIVE`) |

Notes:

- Status string is converted into `GeneralStatus` in the application layer.
- Unknown status strings are mapped to `GeneralStatus.UNKNOWN`.

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `AnnouncementResponse`.

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "title": "Title",
    "subTitle": "Subtitle",
    "description": "Description",
    "position": 1,
    "url": "https://example.com",
    "status": "Inactive"
  },
  "message": "Success",
  "errorCode": 0
}
```

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).
- The use case rejects empty/blank status.

---

### 404 Not Found

Returned when the announcement doesn’t exist.

---

## Examples

### Update status to INACTIVE

```http
PATCH /v1/api/announcements/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/status
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "status": "INACTIVE"
}
```

