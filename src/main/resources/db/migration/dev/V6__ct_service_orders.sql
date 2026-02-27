CREATE TABLE service_orders (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_id UUID REFERENCES appointments(id), -- nullable (for walk-ins)
    bike_id UUID NOT NULL REFERENCES bikes(id),
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    total_price NUMERIC(12,2),

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID
);

CREATE INDEX idx_service_orders_bike ON service_orders(bike_id);
CREATE INDEX idx_service_orders_status ON service_orders(status);
CREATE INDEX idx_service_orders_appointment ON service_orders(appointment_id);

CREATE TABLE service_order_details (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    service_order_id UUID NOT NULL REFERENCES service_orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL,
    product_name_snapshot VARCHAR(50) NOT NULL, -- Store product name at time of order for historical accuracy
    price_snapshot NUMERIC(10,2) NOT NULL, -- Store price at time of order for historical accuracy
    technician_id UUID,
    status VARCHAR(50) NOT NULL,

    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,

    deposit_amount NUMERIC(10,2),
    notes TEXT,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID
);

CREATE INDEX idx_service_details_order ON service_order_details(service_order_id);
CREATE INDEX idx_service_details_status ON service_order_details(status);
CREATE INDEX idx_service_details_technician ON service_order_details(technician_id);

CREATE TABLE service_order_detail_comments (
    id UUID PRIMARY KEY,
    service_order_detail_id UUID NOT NULL REFERENCES service_order_details(id) ON DELETE CASCADE,
    comment TEXT NOT NULL,
    image_url VARCHAR(250),

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);