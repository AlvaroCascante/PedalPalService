# PATCH `/v1/api/announcements/{id}` — Update announcement (partial)

Partially updates an existing announcement.

Only fields present in the request body are modified.

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

### Body (`UpdateAnnouncementRequest`)

All fields are optional. If a field is **missing**, it is not updated.

| Field         | Type    | Required | Validation / Behavior                                                                                                   | Description       |
|---------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------|-------------------|
| `title`       | string  | no       | If present and blank → **400** (`{announcement.update.title.blank}`); max 200 (`{announcement.update.title.max}`)       | Title             |
| `subTitle`    | string  | no       | If present and blank → **400** (`{announcement.update.subtitle.blank}`); max 200 (`{announcement.update.subtitle.max}`) | Subtitle          |
| `description` | string  | no       | —                                                                                                                       | Description text  |
| `position`    | integer | no       | —                                                                                                                       | Sort position     |
| `url`         | string  | no       | If present and blank → **400** (`{announcement.update.url.blank}`); max 500 (`{announcement.update.url.max}`)           | Optional link URL |

Notes:

- **Explicit `null` values** are rejected by Jackson for some fields (`Nulls.FAIL`) where configured.

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `AnnouncementResponse`.

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "title": "New title",
    "subTitle": "Subtitle",
    "description": "Description",
    "position": 1,
    "url": "https://example.com",
    "status": "Active"
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
- The use case rejects blank fields.

---

### 404 Not Found

Returned when the announcement doesn’t exist.

---

## Examples

### Update only title

```http
PATCH /v1/api/announcements/9c84b698-b3fc-4c9d-91f1-9bab8a53a466
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "title": "New title"
}
```

