CREATE TABLE bikes (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id uuid NOT NULL,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_public boolean,
    is_external_sync boolean,

    brand VARCHAR(50),
    model VARCHAR(50),
    year int,
    serial_number VARCHAR(50),
    notes text,
    odometer_km int,
    usage_time_minutes int,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);

CREATE UNIQUE INDEX uk_bikes_serial_number_not_null -- Enforce uniqueness of serial_number when it's not null
    ON bikes (serial_number)
    WHERE serial_number IS NOT NULL;

CREATE TABLE bike_components (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,

    component_type uuid NOT NULL, -- references system_codes(id) where category='COMPONENT_TYPE'
    name VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    brand VARCHAR(50),
    model VARCHAR(50),
    notes text,

    odometer_km int,
    usage_time_minutes int,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL,

    CONSTRAINT fk_bc_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_bc_component_type FOREIGN KEY (component_type) REFERENCES system_codes(id)
);

CREATE TABLE bike_history (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,
    reference_id uuid NOT NULL,

    occurred_at TIMESTAMP NOT NULL,
    performed_by UUID NOT NULL,

    type VARCHAR(50) NOT NULL,
    payload TEXT,

    CONSTRAINT fk_bike_history_bike FOREIGN KEY (bike_id) REFERENCES bikes(id) ON DELETE CASCADE
);

CREATE INDEX idx_bike_history_bike_id ON bike_history(bike_id);
CREATE INDEX idx_bike_history_occurred_at ON bike_history(occurred_at);