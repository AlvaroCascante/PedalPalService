-- R__test_data.sql
-- Dev-only repeatable migration: idempotent test data for bikes and components
-- This file is safe to update: Flyway will re-run it when its checksum changes.

-- Helper notes:
-- Uses explicit UUIDs for deterministic records and ON CONFLICT to be idempotent.
-- component_type is resolved from system_codes by category/code.

