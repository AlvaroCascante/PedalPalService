CREATE TABLE bike_suggestions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,

    suggestion_type uuid NOT NULL,
    confidence_score decimal(5,2),

    package_id uuid,
    product_id uuid,

    name varchar NOT NULL,
    description varchar NOT NULL,
    suggested_date date,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar NOT NULL,

    CONSTRAINT fk_bs_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_bs_suggestion_type FOREIGN KEY (suggestion_type) REFERENCES system_codes(id),
    CONSTRAINT fk_bs_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_bs_product FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT chk_bs_package_xor_product
        CHECK (NOT (package_id IS NOT NULL AND product_id IS NOT NULL))
);
