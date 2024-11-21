-- Create processed_data table
CREATE TABLE processed_data (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    resource_id BIGINT NOT NULL,
    processed_value DOUBLE PRECISION NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- Create analytics_reports table
CREATE TABLE analytics_reports (
    id BIGSERIAL PRIMARY KEY,
    generated_at TIMESTAMP NOT NULL,
    report_type VARCHAR(50) NOT NULL,
    report_data JSONB,
    parameters JSONB,
    resource_id BIGINT,
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- Create indexes for better performance
CREATE INDEX idx_processed_data_resource_timestamp ON processed_data(resource_id, timestamp);
CREATE INDEX idx_analytics_reports_resource_type ON analytics_reports(resource_id, report_type);
CREATE INDEX idx_analytics_reports_generated_at ON analytics_reports(generated_at);