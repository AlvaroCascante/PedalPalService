-- R__test_data.sql
-- Dev-only repeatable migration: idempotent test data for bikes and components
-- This file is safe to update: Flyway will re-run it when its checksum changes.

-- Helper notes:
-- Uses explicit UUIDs for deterministic records and ON CONFLICT to be idempotent.
-- component_type is resolved from system_codes by category/code.

INSERT INTO stores (id, name, created_by) VALUES ('11111111-1111-1111-1111-111111111112', 'Moby Bike N Run', '11111111-1111-1111-1111-111111111111') ON CONFLICT (id) DO NOTHING;
INSERT INTO store_locations (store_id, name, website, address, latitude, longitude, phone, status, created_by)
VALUES ('11111111-1111-1111-1111-111111111112', 'San Joaquin', 'https://www.quetoquenana.com', 'San Joaquin de Flores, Hereida, San José, CR', 10.000847746909152, -84.15251747519727, '+506 2265-7676', 'ACTIVE', '11111111-1111-1111-1111-111111111111') ON CONFLICT (id) DO NOTHING;
