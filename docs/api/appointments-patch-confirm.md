# PATCH `/v1/api/appointments/{id}/confirm` — Confirm appointment

Confirms an appointment.

This endpoint uses the `ConfirmAppointmentUseCase` internally and returns a confirmation response that includes the generated `serviceOrderNumber` (if applicable).

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

### Body

No request body.

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is a `ConfirmAppointmentResponse`.

#### Body (`ConfirmAppointmentResponse`)

```json
{
  "data": {
    "id": "ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0",
    "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "storeLocationId": "b98b6c1a-6d72-4b84-9a4a-6bb408f1a11a",
    "scheduledAt": "2026-02-27T16:00:00Z",
    "status": "CONFIRMED",
    "notes": "Please check drivetrain.",
    "serviceOrderNumber": "SO-2026-0000123",
    "requestedServices": [
      {
        "id": "2d8a7f36-6a51-46c5-bbf7-55a040e93ef4",
        "productId": "106fb5fd-9944-4a8d-8f1a-7b7c1c2d7d93",
        "productNameSnapshot": "Chain replacement",
        "priceSnapshot": 49.99
      }
    ]
  },
  "message": "Success",
  "errorCode": 0
}
```

---

### 404 Not Found

Returned when appointment doesn’t exist.

---

## Example

```http
PATCH /v1/api/appointments/ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0/confirm
Authorization: Bearer <jwt>
```

