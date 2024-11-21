-- Create resources table
CREATE TABLE resources (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    capacity DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    latitude VARCHAR(50),
    longitude VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create monitoring_metrics table
CREATE TABLE monitoring_metrics (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    resource_id BIGINT NOT NULL,
    energy_output DECIMAL(10,2) NOT NULL,
    efficiency DECIMAL(5,2) NOT NULL,
    weather_conditions VARCHAR(50),
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- Create utilization_records table
CREATE TABLE utilization_records (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    resource_id BIGINT NOT NULL,
    usage_data DECIMAL(10,2) NOT NULL,
    efficiency DECIMAL(5,2) NOT NULL,
    notes TEXT,
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- Create indexes
CREATE INDEX idx_monitoring_metrics_resource ON monitoring_metrics(resource_id);
CREATE INDEX idx_monitoring_metrics_timestamp ON monitoring_metrics(timestamp);
CREATE INDEX idx_utilization_records_resource ON utilization_records(resource_id);
CREATE INDEX idx_utilization_records_timestamp ON utilization_records(timestamp);