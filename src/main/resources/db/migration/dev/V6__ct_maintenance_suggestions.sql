CREATE TABLE maintenance_suggestions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,

    suggestion_type uuid NOT NULL,
    suggestion_status uuid NOT NULL,

    ai_provider VARCHAR(50),
    ai_model VARCHAR(100),

    raw_prompt TEXT,
    raw_response TEXT,
    processing_attempts INT NOT NULL DEFAULT 0,
    last_error TEXT,

    version bigint NOT NULL DEFAULT 0,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    created_by varchar NOT NULL,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar NOT NULL,

    CONSTRAINT fk_bs_bike FOREIGN KEY (bike_id) REFERENCES bikes(id),
    CONSTRAINT fk_bs_suggestion_type FOREIGN KEY (suggestion_type) REFERENCES system_codes(id)
);

CREATE TABLE maintenance_suggestion_items (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    suggestion_id UUID NOT NULL,

    package_id UUID,
    product_id UUID,

    priority UUID,
    urgency UUID,
    reason TEXT NOT NULL,

    CONSTRAINT fk_items_suggestion
      FOREIGN KEY (suggestion_id)
          REFERENCES maintenance_suggestions(id)
          ON DELETE CASCADE,

    CONSTRAINT fk_bs_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_bs_product FOREIGN KEY (product_id) REFERENCES products(id)
);