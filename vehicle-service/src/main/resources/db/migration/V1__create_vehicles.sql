CREATE TABLE IF NOT EXISTS vehicles (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id    BIGINT NOT NULL,
  vin            VARCHAR(17) UNIQUE,
  make           VARCHAR(100) NOT NULL,
  model          VARCHAR(100) NOT NULL,
  year           INT NOT NULL,
  usage_type     ENUM('PERSONAL','BUSINESS','COMMUTE') DEFAULT 'PERSONAL',
  annual_mileage INT,
  garaging_zip   VARCHAR(10),
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
