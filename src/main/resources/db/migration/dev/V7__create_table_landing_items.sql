CREATE TABLE "landing_items" (
  "id" uuid PRIMARY KEY,
  "title" varchar NOT NULL,
  "subtitle" varchar,
  "description" text,
  "category" varchar NOT NULL,
  "status" varchar NOT NULL,
  "start_at" timestamp,
  "end_at" timestamp,
  "image_url" varchar,
  "link_url" varchar,
  "priority" int DEFAULT 0,
  "metadata" jsonb,
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar,
  version BIGINT NOT NULL
);

CREATE INDEX IF NOT EXISTS "idx_landing_items_category_status" ON "landing_items" ("category", "status");
CREATE INDEX IF NOT EXISTS "idx_landing_items_start_at" ON "landing_items" ("start_at");
CREATE INDEX IF NOT EXISTS "idx_landing_items_priority" ON "landing_items" ("priority");
