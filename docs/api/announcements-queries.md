# Announcements Queries API

This document describes the announcement query endpoints exposed by `AnnouncementController`.

Endpoints covered:

- `GET /v1/api/announcements/{id}` — Get announcement by id
- `GET /v1/api/announcements/active` — Get active announcements

---

## Common security rules

- Requires authentication.
- Roles:
  - `GET /{id}`: `USER` or `ADMIN`
  - `GET /active`: `USER` or `ADMIN`
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Common response type

All endpoints respond with an `ApiResponse` wrapper.

### `AnnouncementResponse`

```json
{
  "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
  "title": "Title",
  "subTitle": "Subtitle",
  "description": "Description",
  "position": 1,
  "url": "https://example.com",
  "status": "Active"
}
```

Notes:

- `status` is a localized label produced by `AnnouncementApiMapper` via `MessageSource` using the domain enum key (`GeneralStatus.getKey()`).

---

## GET `/v1/api/announcements/{id}` — Get by id

Fetches a single announcement by id.

### Path parameters

| Name | Type | Required | Description             |
|------|------|----------|-------------------------|
| `id` | UUID | yes      | Announcement identifier |

### Responses

#### 200 OK

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

#### 404 Not Found

Returned when the announcement doesn’t exist.

---

## GET `/v1/api/announcements/active` — Get active announcements

Fetches all announcements with status `ACTIVE`.

### Responses

#### 200 OK

```json
{
  "data": [
    {
      "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
      "title": "Title",
      "subTitle": "Subtitle",
      "description": "Description",
      "position": 1,
      "url": "https://example.com",
      "status": "Active"
    }
  ],
  "message": "Success",
  "errorCode": 0
}
```

If there are no active announcements, `data` is an empty array:

```json
{ "data": [], "message": "Success", "errorCode": 0 }
```

