CREATE TABLE media (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),

    owner_id uuid NOT NULL, -- The user who owns the media (ADMIN can upload media on behalf of other users, so this is not necessarily the same as created_by)
    reference_id uuid NOT NULL, -- The entity this media is associated with (e.g. bike, service order, etc.)
    reference_type VARCHAR(50) NOT NULL, -- e.g. "bike_image", "service_order_attachment", etc.
    media_type VARCHAR(50) NOT NULL, -- e.g. "image", "video", "document", etc.
    content_type VARCHAR NOT NULL,  -- e.g. "image/jpeg", "video/mp4", "application/pdf", etc.
    is_primary boolean DEFAULT false,

    status VARCHAR(50) NOT NULL, -- PENDING, UPLOADED, PROCESSED, FAILED, etc.
    storage_key VARCHAR NOT NULL, -- The key used to store the media in the storage provider (e.g. S3 key)
    provider VARCHAR(50) NOT NULL,
    provider_asset_id VARCHAR(50) NOT NULL,

    title VARCHAR(50),
    alt_text VARCHAR,

    size_bytes bigint,
    metadata jsonb,

    version bigint NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by uuid NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar
);