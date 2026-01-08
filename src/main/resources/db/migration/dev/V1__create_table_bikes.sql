CREATE TABLE "bikes" (
    "id" uuid PRIMARY KEY,
    "owner_username" varchar NOT NULL,
    "name" varchar NOT NULL,
    "brand" varchar,
    "model" varchar,
    "year" int,
    "type" varchar,
    "status" varchar NOT NULL,
    "serial_number" varchar,
    "notes" text,
    "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "created_by" varchar NOT NULL,
    "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "updated_by" varchar NOT NULL,
    version BIGINT NOT NULL
);
