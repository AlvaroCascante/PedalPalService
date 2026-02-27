# GET `/v1/api/stores/{id}` — Get store by id

Returns a single store.

Supports an optional query parameter to filter returned locations by status.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes      | Store identifier |

### Query parameters

| Name             | Type                               | Required | Description |
|------------------|------------------------------------|----------|-------------|
| `locationStatus` | string (repeatable / set of enums) | no       | Filters `StoreResponse.locations` by status. Example: `?locationStatus=ACTIVE&locationStatus=INACTIVE`. |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `StoreResponse`:

```json
{
  "data": {
    "id": "d5e0c2b2-4b6c-4f0d-9b29-729d2e9f6b79",
    "name": "PedalPal Workshop",
    "locations": [
      {
        "id": "9f2a5b3c-9a7a-4d08-9b0d-2b2f1b0d1f6a",
        "name": "Downtown",
        "website": "https://example.com",
        "address": "123 Main St",
        "latitude": 9.93333,
        "longitude": -84.08333,
        "phone": "+506 2222-2222",
        "timezone": "America/Costa_Rica",
        "status": "ACTIVE"
      }
    ]
  },
  "message": "Success",
  "errorCode": 0
}
```

---

## Example

```http
GET /v1/api/stores/d5e0c2b2-4b6c-4f0d-9b29-729d2e9f6b79?locationStatus=ACTIVE
Authorization: Bearer <jwt>
```

