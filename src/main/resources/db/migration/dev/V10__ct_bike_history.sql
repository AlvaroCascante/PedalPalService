CREATE TABLE bike_history (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    bike_id uuid NOT NULL,
    reference_id uuid NOT NULL,

    occurred_at TIMESTAMP NOT NULL,
    performed_by UUID NOT NULL,

    type VARCHAR(50) NOT NULL,
    payload TEXT,

    CONSTRAINT fk_bike_history_bike FOREIGN KEY (bike_id) REFERENCES bikes(id) ON DELETE CASCADE
);

CREATE INDEX idx_bike_history_bike_id ON bike_history(bike_id);
CREATE INDEX idx_bike_history_occurred_at ON bike_history(occurred_at);