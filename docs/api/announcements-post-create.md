# POST `/v1/api/announcements` — Create announcement

Creates a new landing page announcement.

---

## Security

- Requires authentication.
- Requires role: `ADMIN`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Request

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`CreateAnnouncementRequest`)

| Field         | Type    | Required | Validation (Bean Validation)                                                                   | Description                              |
|---------------|---------|----------|------------------------------------------------------------------------------------------------|------------------------------------------|
| `title`       | string  | yes      | `@NotBlank` (`{announcement.create.title.blank}`), max 200 (`{announcement.create.title.max}`) | Title                                    |
| `subTitle`    | string  | no       | max 200 (`{announcement.create.subtitle.max}`)                                                 | Subtitle                                 |
| `description` | string  | no       | —                                                                                              | Description text                         |
| `position`    | integer | no       | —                                                                                              | Sort position (lower comes first)        |
| `url`         | string  | no       | max 500 (`{announcement.create.url.max}`)                                                      | Optional CTA / link URL                  |
| `status`      | string  | yes      | `@NotNull` (`{announcement.create.status.required}`)                                           | Status code (e.g., `ACTIVE`, `INACTIVE`) |

Notes:

- Status string is converted into `GeneralStatus` in the application layer.
- Unknown status strings are mapped to `GeneralStatus.UNKNOWN`.

---

## Responses

### 201 Created

Returns an `ApiResponse` whose `data` is an `AnnouncementResponse`.

- `Location` header is set to `/api/announcements/{id}`.

Example:

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "title": "Title",
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

> `status` in the response is a localized label produced by `AnnouncementApiMapper` via `MessageSource` using the enum key.

---

### 400 Bad Request

Returned when:

- Authentication is missing (`ForbiddenAccessException`) — `errorCode = 401`.
- Bean validation fails (`MethodArgumentNotValidException`).
- The use case rejects the request (e.g., blank title).

---

## Examples

### Minimal create

```http
POST /v1/api/announcements
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "title": "Title",
  "status": "ACTIVE"
}
```

### Full create

```http
POST /v1/api/announcements
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "title": "Title",
  "subTitle": "Subtitle",
  "description": "Description",
  "position": 1,
  "url": "https://example.com",
  "status": "ACTIVE"
}
```

