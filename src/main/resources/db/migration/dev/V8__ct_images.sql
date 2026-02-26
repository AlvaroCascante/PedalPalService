CREATE TABLE images (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),

    provider varchar NOT NULL,
    provider_asset_id varchar NOT NULL,

    owner_id uuid NOT NULL,
    context_code uuid NOT NULL,
    reference_id uuid,

    position int NOT NULL,
    is_primary boolean DEFAULT false,

    title varchar,
    alt_text varchar,
    metadata jsonb,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar,

CONSTRAINT fk_images_context FOREIGN KEY (context_code) REFERENCES system_codes(id)
);

-- Only one primary image per context/reference
CREATE UNIQUE INDEX ux_images_primary
    ON images (context_code, reference_id)
    WHERE is_primary = true;

-- Deterministic ordering
CREATE UNIQUE INDEX ux_images_position
    ON images (context_code, reference_id, position);
