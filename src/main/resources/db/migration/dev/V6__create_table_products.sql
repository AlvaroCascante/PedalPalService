CREATE TABLE "products" (
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

CREATE TABLE "packages_products" (
    "package_id" uuid NOT NULL,
    "product_id" uuid NOT NULL,
    PRIMARY KEY ("package_id", "product_id")
);

CREATE TABLE "appointments_products" (
    "appointment_id" uuid NOT NULL,
    "product_id" uuid NOT NULL,
    PRIMARY KEY ("appointment_id", "product_id")
);

ALTER TABLE "packages_products" ADD FOREIGN KEY ("package_id") REFERENCES "packages" ("id") ON DELETE CASCADE;
ALTER TABLE "packages_products" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON DELETE CASCADE;
ALTER TABLE "appointments_products" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointments" ("id") ON DELETE CASCADE;
ALTER TABLE "appointments_products" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON DELETE CASCADE;
