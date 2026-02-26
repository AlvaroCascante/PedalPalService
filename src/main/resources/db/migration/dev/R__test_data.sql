-- R__test_data.sql
-- Dev-only repeatable migration: idempotent test data for bikes and components
-- This file is safe to update: Flyway will re-run it when its checksum changes.

-- Helper notes:
-- Uses explicit UUIDs for deterministic records and ON CONFLICT to be idempotent.
-- component_type is resolved from 11111111-1111-1111-1111-111111111111_codes by category/code.

INSERT INTO stores (id, name, created_by)
    VALUES ('11111111-1111-1111-1111-111111111112', 'Moby Bike N Run', '11111111-1111-1111-1111-111111111111') ON CONFLICT (id) DO NOTHING;

INSERT INTO store_locations (store_id, name, website, address, latitude, longitude, phone, timezone, status, created_by)
    VALUES ('11111111-1111-1111-1111-111111111112', 'San Joaquin', 'https://www.quetoquenana.com', 'San Joaquin de Flores, Hereida, San José, CR', 10.000847746909152, -84.15251747519727, '+506 2265-7676', 'America/Costa_Rica', 'ACTIVE', '11111111-1111-1111-1111-111111111111') ON CONFLICT (id) DO NOTHING;

-- Insert sample productEntities
INSERT INTO products (name, description, price, status, created_by) VALUES
    ('Engrase General Premium', 'Engrase General Premium (doble suspensión)', 40000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Engrase General', 'Engrase General', 20000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Engrase Específico', 'Engrase Específico (unitario)', 6000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Engrase Básico', 'Engrase Básico', 10000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Ensamble de bicicleta Full', 'Ensamble de bicicleta Full (doble suspensión)', 20000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Ensamble de bicicleta nueva', 'Ensamble de bicicleta nueva (rígida)', 10000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Lavado Bicicleta', 'Lavado Bicicleta', 10000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Instalación horquilla', 'Instalación horquilla', 7000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Instalación general', 'Instalación general (unitario)', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Diagnóstico general', 'Diagnóstico general', 5000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Centrado de aros', 'Centrado de aros (unitario)', 4000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Armado de aros eco y cent', 'Armado de aros eco y cent (unit)', 7500.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Armado aros Carbón y cent', 'Armado aros Carbón y cent (unit)', 10000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Cambio de llantas TBL', 'Cambio de llantas TBL (unit)', 3000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Cambio de líquido TBL', 'Cambio de líquido (leche) TBL', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Instalación de cinta TBL', 'Instalación de cinta TBL', 4000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Instalación de válvula TBL', 'Instalación de válvula TBL', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Instalación de gusanillo TBL', 'Instalación de gusanillo TBL', 1000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Cambio de llantas', 'Cambio de llantas (unit)', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Cambio neumático', 'Cambio neumático (unit)', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111'),
    ('Diagnóstico ruedas', 'Diagnóstico general ruedas', 2000.00,'ACTIVE', '11111111-1111-1111-1111-111111111111');

-- Insert sample packages (idempotent)
INSERT INTO packages (id, name, description, price, status, created_by)
    VALUES ('11111111-1111-1111-1111-111111111301', 'Mantenimiento Premium', 'Paquete de mantenimiento premium', 60000.00, 'ACTIVE', '11111111-1111-1111-1111-111111111111')
    ON CONFLICT (id) DO NOTHING;
INSERT INTO packages (id, name, description, price, status, created_by)
    VALUES ('11111111-1111-1111-1111-111111111302', 'Mantenimiento Básico', 'Paquete de mantenimiento básico', 30000.00, 'ACTIVE', '11111111-1111-1111-1111-111111111111')
    ON CONFLICT (id) DO NOTHING;
INSERT INTO packages (id, name, description, price, status, created_by)
    VALUES ('11111111-1111-1111-1111-111111111303', 'Tubeless (TBL) Starter', 'Paquete inicial para tubeless', 12000.00, 'ACTIVE', '11111111-1111-1111-1111-111111111111')
    ON CONFLICT (id) DO NOTHING;

-- Link some existing products into packages (idempotent)
INSERT INTO packages_products (package_id, product_id)
SELECT '11111111-1111-1111-1111-111111111301', p.id FROM products p
WHERE p.name IN ('Engrase General Premium', 'Diagnóstico general', 'Lavado Bicicleta')
ON CONFLICT (package_id, product_id) DO NOTHING;

INSERT INTO packages_products (package_id, product_id)
SELECT '11111111-1111-1111-1111-111111111302', p.id FROM products p
WHERE p.name IN ('Engrase General', 'Lavado Bicicleta')
ON CONFLICT (package_id, product_id) DO NOTHING;

INSERT INTO packages_products (package_id, product_id)
SELECT '11111111-1111-1111-1111-111111111303', p.id FROM products p
WHERE p.name IN ('Cambio de llantas TBL', 'Cambio de líquido TBL', 'Instalación de cinta TBL', 'Instalación de válvula TBL', 'Instalación de gusanillo TBL')
ON CONFLICT (package_id, product_id) DO NOTHING;

