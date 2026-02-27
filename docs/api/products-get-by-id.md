# GET `/v1/api/products/{id}` — Get product by id

Returns a single product.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes      | Product identifier |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `ProductResponse`:

```json
{
  "data": {
    "id": "106fb5fd-9944-4a8d-8f1a-7b7c1c2d7d93",
    "name": "Chain replacement",
    "description": "Replace worn chain",
    "price": 49.99,
    "status": "ACTIVE"
  },
  "message": "Success",
  "errorCode": 0
}
```

---

### 404 Not Found

Returned when the product doesn’t exist.

---

## Example

```http
GET /v1/api/products/106fb5fd-9944-4a8d-8f1a-7b7c1c2d7d93
Authorization: Bearer <jwt>
```

