# POST `/v1/api/appointments` — Create an appointment

Creates a new appointment for the authenticated user.

The request supports an optional list of requested services. Each requested service will be stored using **snapshot fields** (product id, product name snapshot, price snapshot).

---

## Security

- Requires authentication.
- Requires role: `USER`.

---

## Request

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body (`CreateAppointmentRequest`)

| Field               | Type    | Required | Validation                                                   | Description                          |
|---------------------|---------|----------|--------------------------------------------------------------|--------------------------------------|
| `bikeId`            | UUID    | yes      | `@NotNull` (`{appointment.create.bikeId.required}`)          | Bike identifier                      |
| `storeLocationId`   | UUID    | yes      | `@NotNull` (`{appointment.create.storeLocationId.required}`) | Store location identifier            |
| `scheduledAt`       | Instant | yes      | `@NotNull` (`{appointment.create.scheduledAt.required}`)     | Scheduled date/time (UTC / ISO-8601) |
| `notes`             | string  | no       | —                                                            | Notes                                |
| `requestedServices` | array   | no       | `@Valid` (validates items)                                   | List of requested services           |

#### Requested services (`requestedServices[]`)

Each item is a `CreateAppointmentRequest.RequestedServiceRequestItem`:

| Field       | Type | Required | Validation                                                     | Description        |
|-------------|------|----------|----------------------------------------------------------------|--------------------|
| `productId` | UUID | yes      | `@NotNull` (`{appointment.create.service.productId.required}`) | Product identifier |

---

## Responses

### 201 Created

Returns an `ApiResponse` whose `data` is an `AppointmentResponse`.

- `Location` header is set to `/api/appointments/{id}`.

#### Body (`AppointmentResponse`)

```json
{
  "data": {
    "id": "ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0",
    "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "storeLocationId": "b98b6c1a-6d72-4b84-9a4a-6bb408f1a11a",
    "scheduledAt": "2026-02-27T16:00:00Z",
    "status": "REQUESTED",
    "notes": "Please check drivetrain.",
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

### 400 Bad Request

Returned when bean validation fails.

---

### 404 Not Found

Returned when referenced identifiers don’t exist (for example, bike id or product id).

---

## Examples

### Minimal create

```http
POST /v1/api/appointments
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
  "storeLocationId": "b98b6c1a-6d72-4b84-9a4a-6bb408f1a11a",
  "scheduledAt": "2026-02-27T16:00:00Z"
}
```

### Create with requested services

```http
POST /v1/api/appointments
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
  "storeLocationId": "b98b6c1a-6d72-4b84-9a4a-6bb408f1a11a",
  "scheduledAt": "2026-02-27T16:00:00Z",
  "notes": "Please check drivetrain.",
  "requestedServices": [
    { "productId": "106fb5fd-9944-4a8d-8f1a-7b7c1c2d7d93" }
  ]
}
```

