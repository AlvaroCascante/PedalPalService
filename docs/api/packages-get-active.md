# GET `/v1/api/packages/active` — Get active product packages

Returns active product packages.

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

Returns an `ApiResponse` whose `data` is an array of `ProductPackageResponse`.

---

## Example

```http
GET /v1/api/packages/active
Authorization: Bearer <jwt>
```

