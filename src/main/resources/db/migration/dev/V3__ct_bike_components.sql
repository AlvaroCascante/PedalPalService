CREATE TABLE bike_components (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,

    component_type uuid NOT NULL,
    name varchar NOT NULL,
    brand varchar,
    model varchar,
    notes text,

    odometer_km int,
    usage_time_minutes int,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP NULL,
    updated_by uuid NULL,

    CONSTRAINT fk_bc_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_bc_component_type FOREIGN KEY (component_type) REFERENCES system_codes(id)
);
