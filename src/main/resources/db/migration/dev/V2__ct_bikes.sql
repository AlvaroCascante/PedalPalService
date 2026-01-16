CREATE TABLE bikes (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id uuid NOT NULL,

    name varchar NOT NULL,
    brand varchar,
    model varchar,
    year int,
    serial_number varchar,
    notes text,
    is_public boolean DEFAULT false,

    type_id uuid,
    status_id uuid NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar NOT NULL,

    CONSTRAINT fk_bikes_status FOREIGN KEY (status_id) REFERENCES system_codes(id),
    CONSTRAINT fk_bikes_type FOREIGN KEY (type_id) REFERENCES system_codes(id)
);