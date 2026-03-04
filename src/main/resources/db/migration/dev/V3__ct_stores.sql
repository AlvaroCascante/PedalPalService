CREATE TABLE stores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL
);

CREATE TABLE store_locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id UUID NOT NULL,

    name VARCHAR(50) NOT NULL,
    website VARCHAR(100),
    address TEXT,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    phone VARCHAR(50),
    timezone VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL,

    CONSTRAINT fk_sl_store FOREIGN KEY (store_id) REFERENCES stores(id)
);
