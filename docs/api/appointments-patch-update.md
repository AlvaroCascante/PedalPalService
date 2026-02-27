# PATCH `/v1/api/appointments/{id}` — Update appointment details

Partially updates an existing appointment.

This endpoint updates appointment **details** (scheduled time, store location, notes, requested services). It does **not** change the appointment status; use the status endpoint for that.

---

## Security

- Requires authentication.
- Requires role: `USER`.

---

## Request

### Path parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `id` | UUID | yes      | Appointment identifier |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`UpdateAppointmentRequest`)

All fields are optional.

| Field               | Type    | Required | Validation | Description |
|---------------------|---------|----------|------------|-------------|
| `storeLocationId`   | UUID    | no       | —          | New store location id |
| `scheduledAt`       | Instant | no       | —          | New scheduled date/time |
| `notes`             | string  | no       | —          | Notes |
| `requestedServices` | array   | no       | —          | Replaces requested services (full replacement) |

Requested services use the same item shape as create:

```json
{ "productId": "..." }
```

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an `AppointmentResponse`.

---

### 400 Bad Request

Returned when validation fails.

---

### 404 Not Found

Returned when the appointment doesn’t exist.

---

## Examples

### Update only notes

```http
PATCH /v1/api/appointments/ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "notes": "I’ll be 10 minutes late"
}
```

### Update schedule and replace requested services

```http
PATCH /v1/api/appointments/ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "scheduledAt": "2026-03-01T16:00:00Z",
  "requestedServices": [
    { "productId": "106fb5fd-9944-4a8d-8f1a-7b7c1c2d7d93" }
  ]
}
```

