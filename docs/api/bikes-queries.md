# Bikes Queries API

This document describes the bike query endpoints exposed by `BikeController`.

Endpoints covered:

- `GET /v1/api/bikes/{id}` — Get bike by id
- `GET /v1/api/bikes/{id}/history` — Get bike history by bike id
- `GET /v1/api/bikes/active` — Fetch active bikes for the authenticated user

---

## Common security rules

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `CurrentUserProvider`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401** (mapped from `ForbiddenAccessException`).

---

## Common response type

All endpoints respond with an `ApiResponse` wrapper whose `data` is:

- a single `BikeResponse` for `GET /{id}`
- a list of `BikeHistoryResponse` for `GET /{id}/history`
- a list of `BikeResponse` for `GET /active`

### `BikeResponse`

```json
{
  "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
  "name": "My bike",
  "type": "Road",
  "status": "ACTIVE",
  "isPublic": true,
  "isExternalSync": false,
  "brand": "Brand",
  "model": "Model",
  "year": 2020,
  "serialNumber": "SN-123",
  "notes": "Some notes",
  "odometerKm": 1234,
  "usageTimeMinutes": 5678,
  "components": [
    {
      "id": "4e100290-fc14-4d1f-b1f8-9f9338702612",
      "type": "CHAIN",
      "name": "Chain",
      "status": "ACTIVE",
      "brand": "Shimano",
      "model": "HG",
      "notes": "New chain",
      "odometerKm": 10,
      "usageTimeMinutes": 20
    }
  ]
}
```

Notes:

- `type` is a **localized label**, resolved by `BikeApiMapper` via `MessageSource` using the domain enum key.
- `components` is an array of `BikeComponentResponse`. When no components exist, the API returns `components: []`.

### `BikeHistoryResponse`

```json
{
  "id": "1b5bda8d-5c67-4c1f-9643-5f05f3d3e2af",
  "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
  "occurredAt": "2026-02-27T10:15:30",
  "performedBy": "801043b2-f525-402e-87a5-a09641bc628e",
  "type": "BIKE_UPDATED",
  "payload": "{...}"
}
```

---

## GET `/v1/api/bikes/{id}` — Get by id

Fetches a single bike by id for the authenticated user.

### Query parameters

| Name              | Type                               | Required | Description |
|-------------------|------------------------------------|----------|-------------|
| `componentStatus` | string (repeatable / set of enums) | no       | Filters `BikeResponse.components` by component status. Example: `?componentStatus=ACTIVE&componentStatus=INACTIVE`. |

### Path parameters

| Name | Type | Required | Description     |
|------|------|----------|-----------------|
| `id` | UUID | yes      | Bike identifier |

### Responses

#### 200 OK

```json
{
  "data": {
    "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
    "name": "My bike",
    "type": "Road",
    "status": "ACTIVE",
    "isPublic": false,
    "isExternalSync": false,
    "brand": "Brand",
    "model": "Model",
    "year": 2020,
    "serialNumber": null,
    "notes": null,
    "odometerKm": 0,
    "usageTimeMinutes": 0,
    "components": []
  },
  "message": "Success",
  "errorCode": 0
}
```

#### 400 Bad Request

Returned when authentication is missing.

```json
{
  "message": "Authentication is required.",
  "errorCode": 401
}
```

#### 404 Not Found

Returned when the bike doesn’t exist for this user (or the user isn’t the owner).

```json
{
  "message": "Bike not found.",
  "errorCode": 404
}
```

---

## GET `/v1/api/bikes/{id}/history` — Get bike history

Fetches the audit/history events for a bike.

### Path parameters

| Name | Type | Required | Description     |
|------|------|----------|-----------------|
| `id` | UUID | yes      | Bike identifier |

### Responses

#### 200 OK

```json
{
  "data": [
    {
      "id": "1b5bda8d-5c67-4c1f-9643-5f05f3d3e2af",
      "bikeId": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
      "occurredAt": "2026-02-27T10:15:30",
      "performedBy": "801043b2-f525-402e-87a5-a09641bc628e",
      "type": "BIKE_UPDATED",
      "payload": "{...}"
    }
  ],
  "message": "Success",
  "errorCode": 0
}
```

---

## GET `/v1/api/bikes/active` — Fetch active bikes

Fetches all bikes with status `ACTIVE` for the authenticated user.

### Responses

#### 200 OK

```json
{
  "data": [
    {
      "id": "9c84b698-b3fc-4c9d-91f1-9bab8a53a466",
      "name": "My bike",
      "type": "Road",
      "status": "ACTIVE",
      "isPublic": false,
      "isExternalSync": false,
      "brand": "Brand",
      "model": "Model",
      "year": 2020,
      "serialNumber": null,
      "notes": null,
      "odometerKm": 0,
      "usageTimeMinutes": 0,
      "components": []
    }
  ],
  "message": "Success",
  "errorCode": 0
}
```

If there are no active bikes, `data` is an empty array:

```json
{ "data": [], "message": "Success", "errorCode": 0 }
```

#### 400 Bad Request

Returned when authentication is missing.

```json
{
  "message": "Authentication is required.",
  "errorCode": 401
}
```

---

## Examples

### Get by id

```http
GET /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466
Authorization: Bearer <jwt>
```

### Get by id filtering components by status

```http
GET /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466?componentStatus=ACTIVE&componentStatus=INACTIVE
Authorization: Bearer <jwt>
```

### Get history

```http
GET /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/history
Authorization: Bearer <jwt>
```

### Fetch active

```http
GET /v1/api/bikes/active
Authorization: Bearer <jwt>
```
