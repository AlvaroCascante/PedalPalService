CREATE TABLE appointments (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,
    store_location_id uuid NOT NULL,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status varchar NOT NULL,
    notes text,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar,

    CONSTRAINT fk_a_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_a_store_location FOREIGN KEY (store_location_id) REFERENCES store_locations(id)
);

CREATE INDEX idx_appointments_bike ON appointments(bike_id);
CREATE INDEX idx_appointments_scheduled_at ON appointments(scheduled_at);
CREATE INDEX idx_appointments_status ON appointments(status);

CREATE TABLE appointment_requested_service (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_id UUID NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES  products(id),
    product_name_snapshot VARCHAR(255) NOT NULL,
    price_snapshot NUMERIC(10,2)
);