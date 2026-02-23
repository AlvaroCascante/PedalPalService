CREATE TABLE appointments (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,
    store_location_id uuid NOT NULL,
    package_id uuid,

    appointment_date timestamp NOT NULL,
    status varchar NOT NULL,

    odometer_km int,
    total_cost decimal(10,2),
    notes text,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar,

    CONSTRAINT fk_a_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_a_store_location FOREIGN KEY (store_location_id) REFERENCES store_locations(id),
    CONSTRAINT fk_a_package FOREIGN KEY (package_id) REFERENCES packages(id)
);

CREATE TABLE appointments_products (
    appointment_id uuid NOT NULL,
    product_id uuid NOT NULL,

    PRIMARY KEY (appointment_id, product_id),
    CONSTRAINT fk_ap_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    CONSTRAINT fk_ap_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE appointment_tasks (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    appointment_id uuid NOT NULL,

    name varchar NOT NULL,
    description varchar,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar,

    CONSTRAINT fk_at_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

