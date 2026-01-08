CREATE TABLE "store_locations" (
    "id" uuid PRIMARY KEY,
    "store_id" uuid NOT NULL,
    "name" varchar,
    "address" text,
    "latitude" decimal,
    "longitude" decimal,
    "phone" varchar,
    "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "created_by" varchar NOT NULL,
    "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "updated_by" varchar,
    version BIGINT NOT NULL
);

ALTER TABLE "store_locations" ADD FOREIGN KEY ("store_id") REFERENCES "stores" ("id");