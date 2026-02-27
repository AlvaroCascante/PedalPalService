CREATE TABLE media (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),

    provider VARCHAR(50) NOT NULL,
    provider_asset_id VARCHAR(50) NOT NULL,

    owner_id uuid NOT NULL, -- The user who uploaded the media
    reference_id uuid NOT NULL, -- The entity this media is associated with (e.g. bike, service order, etc.)
    context_code VARCHAR(50) NOT NULL, -- e.g. "bike_image", "service_order_attachment", etc.

    is_primary boolean DEFAULT false,

    title VARCHAR(50),
    alt_text VARCHAR,
    metadata jsonb,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar
);