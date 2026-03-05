CREATE TABLE bikes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_public BOOLEAN,
    is_external_sync BOOLEAN,

    brand VARCHAR(50),
    model VARCHAR(50),
    year INT,
    serial_number VARCHAR(50),
    notes VARCHAR,
    odometer_km INT,
    usage_time_minutes INT,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL
);

CREATE UNIQUE INDEX uk_bikes_serial_number_not_null -- Enforce uniqueness of serial_number when it's not null
    ON bikes (serial_number)
    WHERE serial_number IS NOT NULL;

CREATE TABLE bike_components (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id UUID NOT NULL,

    component_type UUID NOT NULL, -- references system_codes(id) where category='COMPONENT_TYPE'
    name VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    brand VARCHAR(50),
    model VARCHAR(50),
    notes VARCHAR,

    odometer_km INT,
    usage_time_minutes INT,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL,

    CONSTRAINT fk_bc_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_bc_component_type FOREIGN KEY (component_type) REFERENCES system_codes(id)
);

CREATE TABLE bike_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id UUID NOT NULL,
    reference_id UUID NOT NULL,

    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    performed_by UUID NOT NULL,

    type VARCHAR(50) NOT NULL,
    payload TEXT,

    CONSTRAINT fk_bike_history_bike FOREIGN KEY (bike_id) REFERENCES bikes(id) ON DELETE CASCADE
);

CREATE INDEX idx_bike_history_bike_id ON bike_history(bike_id);
CREATE INDEX idx_bike_history_occurred_at ON bike_history(occurred_at);