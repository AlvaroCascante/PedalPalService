# GET `/v1/api/packages/{id}` — Get product package by id

Returns a single product package.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description        |
|------|------|----------|--------------------|
| `id` | UUID | yes      | Package identifier |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `ProductPackageResponse`.

`products` is a set of embedded `ProductResponse` items.

---

### 404 Not Found

Returned when the package doesn’t exist.

---

## Example

```http
GET /v1/api/packages/3d9a1d0c-0e74-4c2a-8c83-1a6b78ecf1d2
Authorization: Bearer <jwt>
```

