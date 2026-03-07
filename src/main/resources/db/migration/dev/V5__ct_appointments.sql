CREATE TABLE appointments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    store_location_id UUID NOT NULL,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    closure_reason TEXT,
    deposit NUMERIC(12,2),

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID,

    CONSTRAINT fk_a_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_a_store_location FOREIGN KEY (store_location_id) REFERENCES store_locations(id)
);

CREATE INDEX idx_appointments_bike ON appointments(bike_id);
CREATE INDEX idx_appointments_scheduled_at ON appointments(scheduled_at);
CREATE INDEX idx_appointments_status ON appointments(status);

CREATE TABLE appointment_requested_service (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_id UUID NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES  products(id),
    product_name_snapshot VARCHAR(50) NOT NULL,
    price_snapshot NUMERIC(10,2) NOT NULL
);