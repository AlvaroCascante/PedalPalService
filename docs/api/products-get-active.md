# GET `/v1/api/products/active` — Get active products

Returns active products.

---

## Security

- Requires authentication.
- Requires role: `USER`.

---

## Request

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an array of `ProductResponse`.

---

## Example

```http
GET /v1/api/products/active
Authorization: Bearer <jwt>
```

