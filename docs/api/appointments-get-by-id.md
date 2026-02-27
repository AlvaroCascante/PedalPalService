# GET `/v1/api/appointments/{id}` — Get appointment by id

Returns a single appointment.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes      | Appointment identifier |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `AppointmentResponse`.

---

### 404 Not Found

Returned when the appointment doesn’t exist.

---

## Example

```http
GET /v1/api/appointments/ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0
Authorization: Bearer <jwt>
```

