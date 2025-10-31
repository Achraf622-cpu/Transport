-- PostgreSQL Schema for Transport Delivery Optimization System
-- This schema matches the JPA entities in the project

-- Drop tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS deliveries CASCADE;
DROP TABLE IF EXISTS tours CASCADE;
DROP TABLE IF EXISTS vehicles CASCADE;
DROP TABLE IF EXISTS warehouses CASCADE;

-- Create Warehouses table
CREATE TABLE warehouses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL
);

-- Create Vehicles table
CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    registration_number VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL CHECK (type IN ('VAN', 'TRUCK', 'BIKE')),
    max_weight_kg DOUBLE PRECISION NOT NULL,
    max_volume_m3 DOUBLE PRECISION NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- Create Tours table
CREATE TABLE tours (
    id BIGSERIAL PRIMARY KEY,
    tour_date DATE NOT NULL,
    vehicle_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    algorithm VARCHAR(50) CHECK (algorithm IN ('NEAREST_NEIGHBOR', 'CLARKE_WRIGHT')),
    total_distance_km DOUBLE PRECISION,
    total_weight_kg DOUBLE PRECISION,
    total_volume_m3 DOUBLE PRECISION,
    optimized BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_tour_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    CONSTRAINT fk_tour_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE CASCADE
);

-- Create Deliveries table
CREATE TABLE deliveries (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    weight_kg DOUBLE PRECISION NOT NULL,
    volume_m3 DOUBLE PRECISION NOT NULL,
    preferred_time_slot VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'IN_TRANSIT', 'DELIVERED', 'FAILED')),
    tour_id BIGINT,
    sequence_in_tour INTEGER,
    CONSTRAINT fk_delivery_tour FOREIGN KEY (tour_id) REFERENCES tours(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_vehicles_type ON vehicles(type);
CREATE INDEX idx_vehicles_available ON vehicles(available);
CREATE INDEX idx_deliveries_status ON deliveries(status);
CREATE INDEX idx_deliveries_tour_id ON deliveries(tour_id);
CREATE INDEX idx_tours_tour_date ON tours(tour_date);
CREATE INDEX idx_tours_vehicle_id ON tours(vehicle_id);
CREATE INDEX idx_tours_warehouse_id ON tours(warehouse_id);
CREATE INDEX idx_tours_algorithm ON tours(algorithm);

-- Comments for documentation
COMMENT ON TABLE warehouses IS 'Storage facilities serving as start/end points for delivery tours';
COMMENT ON TABLE vehicles IS 'Fleet of vehicles with different capacity constraints';
COMMENT ON TABLE tours IS 'Planned delivery routes for a specific date and vehicle';
COMMENT ON TABLE deliveries IS 'Individual delivery tasks with GPS coordinates and constraints';

COMMENT ON COLUMN vehicles.type IS 'Vehicle type: VAN (800kg, 10m³), TRUCK (3500kg, 40m³), BIKE (15kg, 0.5m³)';
COMMENT ON COLUMN deliveries.status IS 'Delivery status: PENDING, IN_TRANSIT, DELIVERED, FAILED';
COMMENT ON COLUMN tours.algorithm IS 'Optimization algorithm used: NEAREST_NEIGHBOR or CLARKE_WRIGHT';
