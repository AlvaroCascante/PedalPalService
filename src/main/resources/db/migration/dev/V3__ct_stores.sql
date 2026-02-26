CREATE TABLE stores (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);

CREATE TABLE store_locations (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id uuid NOT NULL,

    name varchar NOT NULL,
    website VARCHAR(100),
    address text,
    latitude decimal(9,6),
    longitude decimal(9,6),
    phone VARCHAR(50),
    timezone VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL,

    CONSTRAINT fk_sl_store FOREIGN KEY (store_id) REFERENCES stores(id)
);
