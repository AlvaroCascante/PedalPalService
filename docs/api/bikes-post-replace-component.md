# POST `/v1/api/bikes/{bikeId}/components/{componentId}/replace` — Replace a bike component

Replaces an existing component on a bike owned by the authenticated user.

Semantics:

- The component identified by `componentId` is removed.
- A new component is created from the request body and added to the bike.
- The updated bike is returned as `BikeResponse`.

---

## Security

- Requires authentication.
- Requires role: `USER`.
- The authenticated user is resolved from a JWT using `SecurityUtils.getCurrentUser()`.

If authentication is missing/invalid, the API returns **400** with an `ApiResponse` whose `errorCode` is **401**.

---

## Request

### Path parameters

| Name          | Type | Required | Description                              |
|---------------|------|----------|------------------------------------------|
| `bikeId`      | UUID | yes      | Bike identifier                          |
| `componentId` | UUID | yes      | Existing component identifier to replace |

### Headers

| Header          | Required | Example            |
|-----------------|----------|--------------------|
| `Content-Type`  | yes      | `application/json` |
| `Authorization` | yes      | `Bearer <jwt>`     |

### Body

This endpoint currently uses the same shape as `AddBikeComponentRequest` (name/type are required, other fields optional).

| Field              | Type   | Required | Validation                              | Description                                                 |
|--------------------|--------|----------|-----------------------------------------|-------------------------------------------------------------|
| `name`             | string | yes      | `{bike.add.component.name.blank}`       | Component name                                              |
| `type`             | string | yes      | `{bike.add.component.type.required}`    | Component type code (system code category `COMPONENT_TYPE`) |
| `brand`            | string | no       | `{bike.add.component.brand.max}`        | Brand                                                       |
| `model`            | string | no       | `{bike.add.component.model.max}`        | Model                                                       |
| `notes`            | string | no       | `{bike.add.component.notes.max}`        | Notes                                                       |
| `odometerKm`       | int    | no       | `{bike.add.component.odometer.invalid}` | Odometer km                                                 |
| `usageTimeMinutes` | int    | no       | `{bike.add.component.usage.invalid}`    | Usage time in minutes                                       |

---

## Responses

### 201 Created

Returns the updated bike wrapped in `ApiResponse`.

---

### 400 Bad Request

Returned when authentication is missing or request validation fails.

---

### 404 Not Found

Returned when:

- Bike doesn’t exist for this user (`RecordNotFoundException("bike.not.found")`).
- Component doesn’t exist on bike (`RecordNotFoundException("bike.component.not.found")`).
- Component type code doesn’t exist (`RecordNotFoundException("bike.component.type.not.found", type)`).

---

## Example

```http
POST /v1/api/bikes/9c84b698-b3fc-4c9d-91f1-9bab8a53a466/components/4e100290-fc14-4d1f-b1f8-9f9338702612/replace
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "name": "Chain",
  "type": "CHAIN",
  "brand": "Shimano",
  "model": "HG",
  "notes": "New chain",
  "odometerKm": 10,
  "usageTimeMinutes": 20
}
```

