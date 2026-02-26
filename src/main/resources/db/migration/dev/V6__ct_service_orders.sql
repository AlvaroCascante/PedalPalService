CREATE TABLE service_orders (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),

    appointment_id UUID REFERENCES appointments(id), -- nullable (for walk-ins)

    bike_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL,

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

CREATE TABLE service_tasks (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),

    service_order_id UUID NOT NULL REFERENCES service_orders(id) ON DELETE CASCADE,

    product_id UUID NOT NULL,

    product_name_snapshot VARCHAR(255) NOT NULL,
    price_snapshot NUMERIC(10,2) NOT NULL,
    duration_snapshot INTEGER,

    technician_id UUID,

    status VARCHAR(30) NOT NULL,

    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,

    notes TEXT,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID
);

CREATE INDEX idx_service_tasks_order ON service_tasks(service_order_id);
CREATE INDEX idx_service_tasks_status ON service_tasks(status);
CREATE INDEX idx_service_tasks_technician ON service_tasks(technician_id);

CREATE TABLE service_task_comments (
    id UUID PRIMARY KEY,
    service_task_id UUID NOT NULL REFERENCES service_tasks(id) ON DELETE CASCADE,
    comment TEXT NOT NULL,
    image_url TEXT NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);