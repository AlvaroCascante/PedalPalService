# GET `/v1/api/stores` — List stores

Returns all stores.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an array of `StoreResponse`.

---

## Example

```http
GET /v1/api/stores
Authorization: Bearer <jwt>
```

