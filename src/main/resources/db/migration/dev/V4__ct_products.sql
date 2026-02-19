CREATE TABLE productEntities (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar NOT NULL,
    description varchar,
    price decimal(10,2),

    status varchar NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar
);

CREATE TABLE packages (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar NOT NULL,
    description varchar,
    price decimal(10,2),

    status varchar NOT NULL,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar
);

CREATE TABLE packages_products (
    package_id uuid NOT NULL,
    product_id uuid NOT NULL,

    PRIMARY KEY (package_id, product_id),
    CONSTRAINT fk_pp_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_pp_product FOREIGN KEY (product_id) REFERENCES productEntities(id)
);
