CREATE TABLE bikes (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id uuid NOT NULL,
    name varchar NOT NULL,
    type varchar NOT NULL,
    status varchar NOT NULL,
    is_public boolean,
    is_external_sync boolean,

    brand varchar,
    model varchar,
    year int,
    serial_number varchar,
    notes text,
    odometer_km int,
    usage_time_minutes int,

    version bigint NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP NULL,
    updated_by uuid NULL
);

CREATE UNIQUE INDEX uk_bikes_serial_number_not_null
    ON bikes (serial_number)
    WHERE serial_number IS NOT NULL;
