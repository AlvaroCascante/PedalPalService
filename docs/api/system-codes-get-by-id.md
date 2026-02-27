# GET `/v1/api/system-codes/{id}` — Get system code by id

Returns a single system code.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes      | System code identifier |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `SystemCodeResponse`:

```json
{
  "data": {
    "id": "44f59f0c-5b50-4a4c-a9a1-8b1a2f807f0a",
    "code": "CHAIN",
    "name": "Chain",
    "status": "ACTIVE",
    "position": 1
  },
  "message": "Success",
  "errorCode": 0
}
```

---

### 404 Not Found

Returned when the system code doesn’t exist.

---

## Example

```http
GET /v1/api/system-codes/44f59f0c-5b50-4a4c-a9a1-8b1a2f807f0a
Authorization: Bearer <jwt>
```

