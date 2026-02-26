CREATE TABLE system_codes (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    category varchar NOT NULL,
    code varchar NOT NULL,
    label varchar,
    description varchar,
    status VARCHAR(50) NOT NULL,
    code_key varchar,
    position int,

    CONSTRAINT ux_system_codes_category_code UNIQUE (category, code)
);

INSERT INTO system_codes (category, code, label, description, status, code_key, position) VALUES
 ('COMPONENT_TYPE', 'CHAIN', 'Chain', 'Chain', 'ACTIVE', 'component.chain', 1),
 ('COMPONENT_TYPE', 'CASSETTE', 'Cassette', 'Cassette', 'ACTIVE', 'component.cassette', 2),
 ('COMPONENT_TYPE', 'CHAINRINGS', 'Chainrings', 'Chainrings', 'ACTIVE', 'component.chainrings', 3),
 ('COMPONENT_TYPE', 'DERAILLEUR_FRONT', 'Front derailleur', 'Front derailleur', 'ACTIVE', 'component.derailleur.front', 4),
 ('COMPONENT_TYPE', 'DERAILLEUR_REAR', 'Rear derailleur', 'Rear derailleur', 'ACTIVE', 'component.derailleur.rear', 5),
 ('COMPONENT_TYPE', 'SHIFTERS', 'Shifters', 'Shifters', 'ACTIVE', 'component.shifters', 6),
 ('COMPONENT_TYPE', 'BRAKE_PADS', 'Brake pads', 'Brake pads', 'ACTIVE', 'component.brake.pads', 7),
 ('COMPONENT_TYPE', 'BRAKE_ROTORS', 'Brake rotors', 'Brake rotors', 'ACTIVE', 'component.brake.rotors', 8),
 ('COMPONENT_TYPE', 'BRAKE_CALIPERS', 'Brake calipers', 'Brake calipers', 'ACTIVE', 'component.brake.calipers', 9),
 ('COMPONENT_TYPE', 'TIRES', 'Tires', 'Tires', 'ACTIVE', 'component.tires', 10),
 ('COMPONENT_TYPE', 'TUBES', 'Tubes', 'Tubes', 'ACTIVE', 'component.tubes', 11),
 ('COMPONENT_TYPE', 'SEALANT', 'Sealant', 'Sealant', 'ACTIVE', 'component.sealant', 12),
 ('COMPONENT_TYPE', 'RIMS', 'Rims', 'Rims', 'ACTIVE', 'component.rims', 13),
 ('COMPONENT_TYPE', 'SPOKES', 'Spokes', 'Spokes', 'ACTIVE', 'component.spokes', 14),
 ('COMPONENT_TYPE', 'BOTTOM_BRACKET', 'Bottom bracket', 'Bottom bracket', 'ACTIVE', 'component.bottom.bracket', 15),
 ('COMPONENT_TYPE', 'HEADSET', 'Headset', 'Headset', 'ACTIVE', 'component.headset', 16),
 ('COMPONENT_TYPE', 'HUBS', 'Hubs', 'Hubs', 'ACTIVE', 'component.hubs', 17),
 ('COMPONENT_TYPE', 'BEARINGS', 'Bearings', 'Bearings', 'ACTIVE', 'component.bearings', 18),
 ('COMPONENT_TYPE', 'SADDLE', 'Saddle', 'Saddle', 'ACTIVE', 'component.saddle', 19),
 ('COMPONENT_TYPE', 'SEAT_POST', 'Seat post', 'Seat post', 'ACTIVE', 'component.seat.post', 20),
 ('COMPONENT_TYPE', 'HANDLEBAR', 'Handlebar', 'Handlebar', 'ACTIVE', 'component.handlebar', 21),
 ('COMPONENT_TYPE', 'STEM', 'Stem', 'Stem', 'ACTIVE', 'component.stem', 22),
 ('COMPONENT_TYPE', 'GRIPS', 'Grips', 'Grips', 'ACTIVE', 'component.grips', 23),
 ('COMPONENT_TYPE', 'BAR_TAPE', 'Bar tape', 'Bar tape', 'ACTIVE', 'component.bar.tape', 24),
 ('COMPONENT_TYPE', 'PEDALS', 'Pedals', 'Pedals', 'ACTIVE', 'component.pedals', 25),
 ('COMPONENT_TYPE', 'FORK', 'Fork', 'Fork', 'ACTIVE', 'component.fork', 26),
 ('COMPONENT_TYPE', 'REAR_SHOCK', 'Rear shock', 'Rear shock', 'ACTIVE', 'component.rear.shock', 27),
 ('COMPONENT_TYPE', 'BATTERY', 'Battery', 'Battery', 'ACTIVE', 'component.battery', 28),
 ('COMPONENT_TYPE', 'MOTOR', 'Motor', 'Motor', 'ACTIVE', 'component.motor', 29),
 ('COMPONENT_TYPE', 'CONTROLLER', 'Controller', 'Controller', 'ACTIVE', 'component.controller', 30),
 ('COMPONENT_TYPE', 'OTHER', 'Other', 'Other', 'ACTIVE', 'component.other', 31)
 ;

