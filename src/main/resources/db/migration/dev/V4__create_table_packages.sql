CREATE TABLE "packages" (
    "id" uuid PRIMARY KEY,
    "name" varchar NOT NULL,
    "description" text,
    "price" decimal(10,2),
    "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "created_by" varchar NOT NULL,
    "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "updated_by" varchar,
    version BIGINT NOT NULL
);