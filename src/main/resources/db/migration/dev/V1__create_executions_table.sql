CREATE TABLE "bikes" (
  "id" uuid PRIMARY KEY,
  "owner_username" varchar NOT NULL,
  "name" varchar NOT NULL,
  "brand" varchar,
  "model" varchar,
  "year" int,
  "type" varchar,
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
  "status" varchar NOT NULL,
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

ALTER TABLE "bike_components" ADD FOREIGN KEY ("bike_id") REFERENCES "bikes" ("id");

ALTER TABLE "appointments" ADD FOREIGN KEY ("bike_id") REFERENCES "bikes" ("id");

ALTER TABLE "bike_components" ADD FOREIGN KEY ("installed_by_appointment_id") REFERENCES "appointments" ("id");

ALTER TABLE "appointment_tasks" ADD FOREIGN KEY ("appointment_id") REFERENCES "appointments" ("id");

ALTER TABLE "store_locations" ADD FOREIGN KEY ("store_id") REFERENCES "stores" ("id");

ALTER TABLE "appointments" ADD FOREIGN KEY ("store_location_id") REFERENCES "store_locations" ("id");
