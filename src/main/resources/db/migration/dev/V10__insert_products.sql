-- Insert sample products
INSERT INTO products (name, description, price, status_id, created_by) VALUES
('Engrase General Premium', 'Engrase General Premium (doble suspensión)', 40000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Engrase General', 'Engrase General', 20000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Engrase Específico', 'Engrase Específico (unitario)', 6000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Engrase Básico', 'Engrase Básico', 10000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Ensamble de bicicleta Full', 'Ensamble de bicicleta Full (doble suspensión)', 20000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Ensamble de bicicleta nueva', 'Ensamble de bicicleta nueva (rígida)', 10000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Lavado Bicicleta', 'Lavado Bicicleta', 10000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Instalación horquilla', 'Instalación horquilla', 7000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Instalación general', 'Instalación general (unitario)', 2000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Diagnóstico general', 'Diagnóstico general', 5000.00,
(select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Centrado de aros', 'Centrado de aros (unitario)', 4000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Armado de aros eco y cent', 'Armado de aros eco y cent (unit)', 7500.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Armado aros Carbón y cent', 'Armado aros Carbón y cent (unit)', 10000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Cambio de llantas TBL', 'Cambio de llantas TBL (unit)', 3000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Cambio de líquido TBL', 'Cambio de líquido (leche) TBL', 2000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Instalación de cinta TBL', 'Instalación de cinta TBL', 4000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Instalación de válvula TBL', 'Instalación de válvula TBL', 2000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Instalación de gusanillo TBL', 'Instalación de gusanillo TBL', 1000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Cambio de llantas', 'Cambio de llantas (unit)', 2000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Cambio neumático', 'Cambio neumático (unit)', 2000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system'),

('Diagnóstico ruedas', 'Diagnóstico general ruedas', 2000.00,
 (select id from system_codes where category = 'GENERAL_STATUS' and code = 'ACTIVE'), 'system');
