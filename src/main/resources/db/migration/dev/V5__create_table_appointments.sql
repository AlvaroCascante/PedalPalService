CREATE TABLE "appointments" (
    "id" uuid PRIMARY KEY,
    "bike_id" uuid NOT NULL,
    "store_location_id" uuid,
    "package_id" uuid,
    "appointment_date" timestamp NOT NULL,
    "status" varchar(100) NOT NULL,
    "odometer_km" int,
    "total_cost" decimal(10,2),
    "notes" text,
    "created_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "created_by" varchar NOT NULL,
    "updated_at" timestamp DEFAULT (CURRENT_TIMESTAMP),
    "updated_by" varchar,
    version BIGINT NOT NULL
);

ALTER TABLE "appointments" ADD FOREIGN KEY ("bike_id") REFERENCES "bikes" ("id");
ALTER TABLE "appointments" ADD FOREIGN KEY ("store_location_id") REFERENCES "stores" ("id") ON DELETE SET NULL;
ALTER TABLE "appointments" ADD FOREIGN KEY ("package_id") REFERENCES "packages" ("id") ON DELETE SET NULL;