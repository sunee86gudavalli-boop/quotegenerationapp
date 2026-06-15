CREATE TABLE IF NOT EXISTS quotes (
  id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
  quote_number          VARCHAR(20) NOT NULL UNIQUE,
  customer_id           BIGINT NOT NULL,
  vehicle_id            BIGINT NOT NULL,
  driver_id             BIGINT NOT NULL,
  coverage_type         ENUM('LIABILITY','STANDARD','FULL') NOT NULL,
  base_premium          DECIMAL(10,2),
  liability_premium     DECIMAL(10,2),
  collision_premium     DECIMAL(10,2),
  comprehensive_premium DECIMAL(10,2),
  total_premium         DECIMAL(10,2) NOT NULL,
  status                ENUM('ACTIVE','EXPIRED','CONVERTED') DEFAULT 'ACTIVE',
  created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at            TIMESTAMP
);
