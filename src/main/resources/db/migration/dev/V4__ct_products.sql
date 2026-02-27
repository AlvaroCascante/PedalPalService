CREATE TABLE products (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    description text,
    price decimal(10,2),

    status VARCHAR(50) NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);

CREATE TABLE packages (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    description text,
    price decimal(10,2),

    status VARCHAR(50) NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by uuid NULL
);

CREATE TABLE packages_products (
    package_id uuid NOT NULL,
    product_id uuid NOT NULL,

    PRIMARY KEY (package_id, product_id),
    CONSTRAINT fk_pp_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_pp_product FOREIGN KEY (product_id) REFERENCES products(id)
);
