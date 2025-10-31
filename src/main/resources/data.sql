-- Sample data for testing the Transport application

-- Insert Warehouses (without explicit IDs - let sequences handle it)
INSERT INTO warehouses (name, address, latitude, longitude, opening_time, closing_time) VALUES
('Paris Main Depot', '123 Warehouse St, Paris', 48.8566, 2.3522, '06:00:00', '22:00:00'),
('Lyon Distribution Center', '456 Depot Ave, Lyon', 45.7640, 4.8357, '06:00:00', '22:00:00');

-- Insert Vehicles (using snake_case column names)
INSERT INTO vehicles (registration_number, type, max_weight_kg, max_volumem3, available) VALUES
('VAN-001', 'VAN', 800.0, 10.0, true),
('VAN-002', 'VAN', 800.0, 10.0, true),
('TRUCK-001', 'TRUCK', 3500.0, 40.0, true),
('BIKE-001', 'BIKE', 15.0, 0.5, true);

-- Insert Deliveries (Paris area - using snake_case column names)
INSERT INTO deliveries (address, latitude, longitude, weight_kg, volumem3, preferred_time_slot, status, tour_id, sequence_in_tour) VALUES
('10 Rue de Rivoli, Paris', 48.8606, 2.3376, 5.5, 0.3, '09:00-11:00', 'PENDING', NULL, NULL),
('25 Avenue des Champs-Élysées, Paris', 48.8698, 2.3078, 8.2, 0.5, '10:00-12:00', 'PENDING', NULL, NULL),
('15 Rue du Faubourg Saint-Honoré, Paris', 48.8708, 2.3161, 12.0, 0.8, '11:00-13:00', 'PENDING', NULL, NULL),
('50 Boulevard Haussmann, Paris', 48.8738, 2.3264, 6.5, 0.4, '14:00-16:00', 'PENDING', NULL, NULL),
('100 Rue de la Pompe, Paris', 48.8634, 2.2773, 15.0, 1.0, '09:00-11:00', 'PENDING', NULL, NULL),
('30 Rue de Passy, Paris', 48.8575, 2.2844, 9.0, 0.6, '10:00-12:00', 'PENDING', NULL, NULL),
('75 Avenue Kléber, Paris', 48.8713, 2.2930, 11.5, 0.7, '13:00-15:00', 'PENDING', NULL, NULL),
('20 Rue de Belleville, Paris', 48.8722, 2.3825, 7.8, 0.5, '09:00-11:00', 'PENDING', NULL, NULL),
('45 Boulevard de Ménilmontant, Paris', 48.8654, 2.3854, 13.2, 0.9, '11:00-13:00', 'PENDING', NULL, NULL),
('88 Rue Oberkampf, Paris', 48.8648, 2.3776, 10.5, 0.7, '14:00-16:00', 'PENDING', NULL, NULL);
