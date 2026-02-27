# GET `/v1/api/appointments/bike/{bikeId}/upcoming` — Upcoming appointments

Returns upcoming appointments for a bike.

This endpoint is backed by the `AppointmentQueryService` and returns a lightweight list item response.

---

## Security

- Requires authentication.
- Requires role: `USER`.

---

## Request

### Path parameters

| Name     | Type | Required | Description     |
|----------|------|----------|-----------------|
| `bikeId` | UUID | yes      | Bike identifier |

### Headers

| Header          | Required | Example        |
|-----------------|----------|----------------|
| `Authorization` | yes      | `Bearer <jwt>` |

---

## Responses

### 200 OK

Returns an `ApiResponse` whose `data` is an array of `AppointmentListItemResponse`:

```json
{
  "data": [
    {
      "id": "ad1d92c1-91cb-4c3b-9b39-e73a39d4cfe0",
      "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
      "storeLocationId": "b98b6c1a-6d72-4b84-9a4a-6bb408f1a11a",
      "scheduledAt": "2026-02-27T16:00:00Z",
      "status": "REQUESTED"
    }
  ],
  "message": "Success",
  "errorCode": 0
}
```

---

## Example

```http
GET /v1/api/appointments/bike/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/upcoming
Authorization: Bearer <jwt>
```

