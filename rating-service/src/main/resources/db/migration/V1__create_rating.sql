CREATE TABLE IF NOT EXISTS rating_factors (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  factor_name  VARCHAR(100),
  factor_key   VARCHAR(100),
  factor_value DECIMAL(5,3),
  description  VARCHAR(255)
);

INSERT INTO rating_factors (factor_name, factor_key, factor_value, description) VALUES
  ('Base Premium',        'BASE_PREMIUM',      800.000, 'Annual base premium'),
  ('Young Driver',        'AGE_UNDER_25',      1.400,   'Surcharge multiplier for drivers under 25'),
  ('Senior Driver',       'AGE_OVER_70',       1.200,   'Surcharge multiplier for drivers over 70'),
  ('Per Violation',       'VIOLATION_AMOUNT',  150.000, 'Added amount per traffic violation'),
  ('Per Accident',        'ACCIDENT_AMOUNT',   200.000, 'Added amount per at-fault accident'),
  ('Old Vehicle',         'OLD_VEHICLE',       0.900,   'Discount for vehicles older than 10 years'),
  ('High Mileage',        'HIGH_MILEAGE',      1.100,   'Surcharge for annual mileage > 15000'),
  ('Liability Factor',    'LIABILITY_FACTOR',  0.400,   'Liability premium as fraction of base'),
  ('Collision Factor',    'COLLISION_FACTOR',  0.350,   'Collision premium as fraction of base'),
  ('Comprehensive Factor','COMPREHENSIVE_FACTOR',0.250, 'Comprehensive premium as fraction of base');
