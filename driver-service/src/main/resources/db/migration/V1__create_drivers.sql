CREATE TABLE IF NOT EXISTS drivers (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id        BIGINT NOT NULL,
  first_name         VARCHAR(100),
  last_name          VARCHAR(100),
  date_of_birth      DATE,
  license_number     VARCHAR(50),
  license_state      CHAR(2),
  years_licensed     INT,
  violations_count   INT DEFAULT 0,
  accidents_count    INT DEFAULT 0,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
