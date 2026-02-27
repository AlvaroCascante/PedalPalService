# PATCH `/v1/api/appointments/{id}/status` — Update appointment status

Updates only the appointment **status**.

---

## Security

- Requires authentication.
- Requires role: `USER` or `ADMIN`.

---

## Request

### Path parameters

| Name | Type | Required | Description            |
|------|------|----------|------------------------|
| `id` | UUID | yes      | Appointment identifier |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateAppointmentStatusRequest`)

| Field    | Type   | Required | Validation                                          | Description     |
|----------|--------|----------|-----------------------------------------------------|-----------------|
| `status` | string | yes      | `@NotNull` (`{appointment.update.status.required}`) | New status code |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `AppointmentResponse`.

---

### 400 Bad Request

Returned when validation fails.

---

### 404 Not Found

Returned when appointment doesn’t exist.

---

## Examples

```http
PATCH /v1/api/appointments/ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0/status
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "status": "CONFIRMED"
}
```

