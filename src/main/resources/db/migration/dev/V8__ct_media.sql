CREATE TABLE media (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    reference_id UUID NOT NULL, -- The entity this media is associated with (e.g. bike, service order, etc.)
    reference_type VARCHAR(50) NOT NULL, -- e.g. "bike_image", "service_order_attachment", etc.
    content_type VARCHAR NOT NULL,  -- e.g. "image/jpeg", "video/mp4", "application/pdf", etc.
    provider VARCHAR(50) NOT NULL,
    is_primary BOOLEAN DEFAULT false,
    status VARCHAR(50) NOT NULL, -- DRAFT, ACTIVE, INACTIVE, FAILED, etc.
    storage_key VARCHAR NOT NULL, -- The key used to store the media in the storage provider (e.g. S3 key)
    name VARCHAR(50),
    alt_text VARCHAR,

    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID
);