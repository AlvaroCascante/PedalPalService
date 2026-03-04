CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    description VARCHAR,
    price DECIMAL(10,2),

    status VARCHAR(50) NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL
);

CREATE TABLE packages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    description VARCHAR,
    price decimal(10,2),

    status VARCHAR(50) NOT NULL,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL
);

CREATE TABLE packages_products (
    package_id UUID NOT NULL,
    product_id UUID NOT NULL,

    PRIMARY KEY (package_id, product_id),
    CONSTRAINT fk_pp_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_pp_product FOREIGN KEY (product_id) REFERENCES products(id)
);
