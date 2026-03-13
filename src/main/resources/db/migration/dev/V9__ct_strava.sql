CREATE TABLE strava_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    strava_athlete_id BIGINT NOT NULL,
    access_token VARCHAR(2048) NOT NULL,
    refresh_token VARCHAR(2048) NOT NULL,
    token_expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    scope VARCHAR(512),
    status VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL
);

CREATE UNIQUE INDEX uk_strava_connections_user_id ON strava_connections(user_id);
CREATE UNIQUE INDEX uk_strava_connections_athlete_id ON strava_connections(strava_athlete_id);

CREATE TABLE strava_activity_sync (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    bike_id UUID NOT NULL,
    strava_activity_id BIGINT NOT NULL,
    strava_athlete_id BIGINT NOT NULL,
    distance_km_delta INT NOT NULL,
    moving_time_minutes_delta INT NOT NULL,
    original_distance_meters BIGINT,
    original_moving_time_seconds BIGINT,
    sport_type VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    synced_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by UUID NULL,
    CONSTRAINT fk_strava_activity_bike FOREIGN KEY (bike_id) REFERENCES bikes(id)
);

CREATE UNIQUE INDEX uk_strava_activity_id ON strava_activity_sync(strava_activity_id);
CREATE INDEX idx_strava_activity_bike_id ON strava_activity_sync(bike_id);

