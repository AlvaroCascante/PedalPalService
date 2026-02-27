CREATE TABLE maintenance_suggestions (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,

    suggestion_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    ai_provider VARCHAR(50),
    ai_model VARCHAR(100),

    raw_prompt TEXT,
    raw_response TEXT,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar NOT NULL,

    CONSTRAINT fk_bs_bike FOREIGN KEY (bike_id) REFERENCES bikes(id)
);

CREATE TABLE maintenance_suggestion_items (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    suggestion_id UUID NOT NULL,

    package_id UUID,
    product_id UUID,

    priority VARCHAR(50),
    urgency VARCHAR(50),
    reason TEXT NOT NULL,

    CONSTRAINT fk_items_suggestion
      FOREIGN KEY (suggestion_id)
          REFERENCES maintenance_suggestions(id)
          ON DELETE CASCADE,

    CONSTRAINT fk_bs_package FOREIGN KEY (package_id) REFERENCES packages(id),
    CONSTRAINT fk_bs_product FOREIGN KEY (product_id) REFERENCES products(id)
);