-- R__test_data.sql
-- Dev-only repeatable migration: idempotent test data for bikes and components
-- This file is safe to update: Flyway will re-run it when its checksum changes.

-- Helper notes:
-- Uses explicit UUIDs for deterministic records and ON CONFLICT to be idempotent.
-- component_type is resolved from system_codes by category/code.

-- Insert 10 bikes
INSERT INTO bikes (id, owner_id, name, type, status, is_public, is_external_sync, brand, model, year, serial_number, notes, odometer_km, usage_time_minutes, version, created_at, created_by, updated_at, updated_by) VALUES
('11111111-1111-1111-1111-111111111111','aaaaaaaa-0000-0000-0000-000000000001','Dev Bike 01','ROAD','ACTIVE',false,false,'BrandA','Model-A1',2020,'SN-A-0001','Test bike 1',1000,120,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111112','aaaaaaaa-0000-0000-0000-000000000002','Dev Bike 02','ROAD','ACTIVE',false,false,'BrandB','Model-B1',2019,'SN-B-0002','Test bike 2',2000,240,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111113','aaaaaaaa-0000-0000-0000-000000000003','Dev Bike 03','GRAVEL','ACTIVE',false,false,'BrandC','Model-C1',2021,'SN-C-0003','Test bike 3',3000,360,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111114','aaaaaaaa-0000-0000-0000-000000000004','Dev Bike 04','HYBRID','ACTIVE',false,false,'BrandD','Model-D1',2018,'SN-D-0004','Test bike 4',4000,480,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111115','aaaaaaaa-0000-0000-0000-000000000005','Dev Bike 05','ELECTRIC','ACTIVE',true,false,'BrandE','Model-E1',2017,'SN-E-0005','Test bike 5',5000,600,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111116','aaaaaaaa-0000-0000-0000-000000000006','Dev Bike 06','URBAN','ACTIVE',true,false,'BrandF','Model-F1',2016,'SN-F-0006','Test bike 6',6000,720,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111117','aaaaaaaa-0000-0000-0000-000000000007','Dev Bike 07','MOUNTAIN','ACTIVE',false,true,'BrandG','Model-G1',2022,'SN-G-0007','Test bike 7',7000,840,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111118','aaaaaaaa-0000-0000-0000-000000000008','Dev Bike 08','ROAD','ACTIVE',false,true,'BrandH','Model-H1',2015,'SN-H-0008','Test bike 8',8000,960,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111119','aaaaaaaa-0000-0000-0000-000000000009','Dev Bike 09','GRAVEL','ACTIVE',false,false,'BrandI','Model-I1',2014,'SN-I-0009','Test bike 9',9000,1080,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL),
('11111111-1111-1111-1111-111111111120','aaaaaaaa-0000-0000-0000-000000000010','Dev Bike 10','ROAD','ACTIVE',true,true,'BrandJ','Model-J1',2023,'SN-J-0010','Test bike 10',10000,1200,0,CURRENT_TIMESTAMP,'11111111-0000-1111-1111-111111111111',NULL,NULL)
ON CONFLICT (id) DO UPDATE SET owner_id=EXCLUDED.owner_id,name=EXCLUDED.name,type=EXCLUDED.type,status=EXCLUDED.status,is_public=EXCLUDED.is_public,is_external_sync=EXCLUDED.is_external_sync,brand=EXCLUDED.brand,model=EXCLUDED.model,year=EXCLUDED.year,serial_number=EXCLUDED.serial_number,notes=EXCLUDED.notes,odometer_km=EXCLUDED.odometer_km,usage_time_minutes=EXCLUDED.usage_time_minutes,version=EXCLUDED.version,created_at=EXCLUDED.created_at,created_by=EXCLUDED.created_by,updated_at=EXCLUDED.updated_at,updated_by=EXCLUDED.updated_by;

-- Insert components for each bike (3 components per bike: CHAIN, TIRES, BRAKE_PADS)

INSERT INTO bike_components (id, bike_id, component_type, name, brand, model, notes, created_by, updated_by)
VALUES
  ('22222222-2222-2222-2222-222222222201', '11111111-1111-1111-1111-111111111111', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 01', 'Shimano', 'CN-A1', 'Chain for Dev Bike 01', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222202', '11111111-1111-1111-1111-111111111111', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 01', 'Continental', 'TR-A1', 'Front tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222203', '11111111-1111-1111-1111-111111111111', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 01', 'SRAM', 'BP-A1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222204', '11111111-1111-1111-1111-111111111112', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 02', 'Shimano', 'CN-B1', 'Chain for Dev Bike 02', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222205', '11111111-1111-1111-1111-111111111112', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 02', 'Maxxis', 'TR-B1', 'Rear tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222206', '11111111-1111-1111-1111-111111111112', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 02', 'Avid', 'BP-B1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222207', '11111111-1111-1111-1111-111111111113', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 03', 'KMC', 'CN-C1', 'Chain for Dev Bike 03', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222208', '11111111-1111-1111-1111-111111111113', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 03', 'Schwalbe', 'TR-C1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222209', '11111111-1111-1111-1111-111111111113', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 03', 'Shimano', 'BP-C1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222210', '11111111-1111-1111-1111-111111111114', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 04', 'Shimano', 'CN-D1', 'Chain for Dev Bike 04', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222211', '11111111-1111-1111-1111-111111111114', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 04', 'Vittoria', 'TR-D1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222212', '11111111-1111-1111-1111-111111111114', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 04', 'SRAM', 'BP-D1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222213', '11111111-1111-1111-1111-111111111115', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 05', 'KMC', 'CN-E1', 'Chain for Dev Bike 05', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222214', '11111111-1111-1111-1111-111111111115', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 05', 'Pirelli', 'TR-E1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222215', '11111111-1111-1111-1111-111111111115', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 05', 'Avid', 'BP-E1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222216', '11111111-1111-1111-1111-111111111116', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 06', 'Shimano', 'CN-F1', 'Chain for Dev Bike 06', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222217', '11111111-1111-1111-1111-111111111116', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 06', 'Continental', 'TR-F1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222218', '11111111-1111-1111-1111-111111111116', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 06', 'Shimano', 'BP-F1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222219', '11111111-1111-1111-1111-111111111117', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 07', 'KMC', 'CN-G1', 'Chain for Dev Bike 07', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222220', '11111111-1111-1111-1111-111111111117', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 07', 'Maxxis', 'TR-G1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222221', '11111111-1111-1111-1111-111111111117', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 07', 'Avid', 'BP-G1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111118', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 08', 'KMC', 'CN-H1', 'Chain for Dev Bike 08', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222223', '11111111-1111-1111-1111-111111111118', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 08', 'Vittoria', 'TR-H1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222224', '11111111-1111-1111-1111-111111111118', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 08', 'SRAM', 'BP-H1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222225', '11111111-1111-1111-1111-111111111119', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 09', 'Shimano', 'CN-I1', 'Chain for Dev Bike 09', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222226', '11111111-1111-1111-1111-111111111119', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 09', 'Schwalbe', 'TR-I1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222227', '11111111-1111-1111-1111-111111111119', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 09', 'Avid', 'BP-I1', 'Brake pads', 'dev', 'dev'),

  ('22222222-2222-2222-2222-222222222228', '11111111-1111-1111-1111-111111111120', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'CHAIN'), 'Chain 10', 'KMC', 'CN-J1', 'Chain for Dev Bike 10', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222229', '11111111-1111-1111-1111-111111111120', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'TIRES'), 'Tire 10', 'Pirelli', 'TR-J1', 'Tire', 'dev', 'dev'),
  ('22222222-2222-2222-2222-222222222230', '11111111-1111-1111-1111-111111111120', (SELECT id FROM system_codes WHERE category = 'COMPONENT_TYPE' AND code = 'BRAKE_PADS'), 'Brake Pads 10', 'Shimano', 'BP-J1', 'Brake pads', 'dev', 'dev');

-- End of test data

