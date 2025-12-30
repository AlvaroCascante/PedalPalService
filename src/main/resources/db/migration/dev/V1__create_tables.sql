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
  "updated_by" varchar NOT NULL
);

CREATE TABLE "bike_components" (
  "id" uuid PRIMARY KEY,
  "bike_id" uuid NOT NULL,
  "component_type" varchar NOT NULL,
  "name" varchar NOT NULL,
  "brand" varchar,
  "model" varchar,
  "notes" text,
  "installed_at" date,
  "installed_by" varchar NOT NULL,
  "installed_odometer_km" int,
  "removed_at" date,
  "removed_by" varchar,
  "removed_odometer_km" int,
  "installed_by_appointment_id" uuid
);

CREATE TABLE "stores" (
  "id" uuid PRIMARY KEY,
  "name" varchar NOT NULL,
  "website" varchar,
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar
);

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
  "updated_by" varchar
);

CREATE TABLE "appointments" (
  "id" uuid PRIMARY KEY,
  "bike_id" uuid NOT NULL,
  "store_location_id" uuid,
  "appointment_date" timestamp NOT NULL,
  "status" varchar(100) NOT NULL,
  "odometer_km" int,
  "total_cost" decimal(10,2),
  "notes" text,
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar
);

CREATE TABLE "appointment_tasks" (
  "id" uuid PRIMARY KEY,
  "appointment_id" uuid NOT NULL,
  "description" varchar NOT NULL,
  "labor_cost" decimal(10,2),
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar
);

CREATE TABLE "products" (
  "id" uuid PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" text,
  "price" decimal(10,2),
  "duration_minutes" int,
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar
);

CREATE TABLE "service_packages" (
  "id" uuid PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" text,
  "price" decimal(10,2),
  "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "created_by" varchar NOT NULL,
  "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
  "updated_by" varchar
);

-- Join table: package_products (service_package <-> product)
CREATE TABLE "package_products" (
  "package_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  PRIMARY KEY ("package_id", "product_id")
);

-- Join table: appointment_products (appointment <-> product)
CREATE TABLE "appointment_products" (
  "appointment_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  PRIMARY KEY ("appointment_id", "product_id")
);

ALTER TABLE "bike_components" ADD FOREIGN KEY ("bike_id") REFERENCES "bikes" ("id");

ALTER TABLE "appointments" ADD FOREIGN KEY ("bike_id") REFERENCES "bikes" ("id");

ALTER TABLE "bike_components" ADD FOREIGN KEY ("installed_by_appointment_id") REFERENCES "appointments" ("id");

ALTER TABLE "appointment_tasks" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointments" ("id");

ALTER TABLE "store_locations" ADD FOREIGN KEY ("store_id") REFERENCES "stores" ("id");

ALTER TABLE "appointments" ADD FOREIGN KEY ("store_location_id") REFERENCES "store_locations" ("id");

-- Foreign keys for new tables
ALTER TABLE "package_products" ADD FOREIGN KEY ("package_id") REFERENCES "service_packages" ("id") ON DELETE CASCADE;
ALTER TABLE "package_products" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON DELETE CASCADE;

ALTER TABLE "appointment_products" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointments" ("id") ON DELETE CASCADE;
ALTER TABLE "appointment_products" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON DELETE CASCADE;

-- Add reference from appointments to service_packages (nullable)
ALTER TABLE "appointments" ADD COLUMN IF NOT EXISTS "service_package_id" uuid;
ALTER TABLE "appointments" ADD FOREIGN KEY ("service_package_id") REFERENCES "service_packages" ("id") ON DELETE SET NULL;

-- Landing items table for the app landing/home feed
CREATE TABLE IF NOT EXISTS "landing_items" (
  "id" uuid PRIMARY KEY,
  "title" varchar NOT NULL,
  "subtitle" varchar,
  "description" text,
  -- category and status are handled in the application layer; keep DB flexible
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
  "updated_by" varchar
);

CREATE INDEX IF NOT EXISTS "idx_landing_items_category_status" ON "landing_items" ("category", "status");
CREATE INDEX IF NOT EXISTS "idx_landing_items_start_at" ON "landing_items" ("start_at");
CREATE INDEX IF NOT EXISTS "idx_landing_items_priority" ON "landing_items" ("priority");

-- End of migration
