# GET `/v1/api/components` — Get active component types

Returns active component types.

This endpoint returns **system codes** for the `COMPONENT_TYPE` category (i.e., the catalog of component types such as `CHAIN`, `CASSETTE`, etc.).

It is not related to "bike components" CRUD: those are managed via `/v1/api/bikes/{id}/components`.

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

Returns an `ApiResponse` whose `data` is an array of `SystemCodeResponse`.

---

## Example

```http
GET /v1/api/components
Authorization: Bearer <jwt>
```

