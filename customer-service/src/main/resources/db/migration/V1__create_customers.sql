CREATE TABLE IF NOT EXISTS customers (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  phone         VARCHAR(20),
  date_of_birth DATE NOT NULL,
  address       VARCHAR(255),
  city          VARCHAR(100),
  state         CHAR(2),
  zip_code      VARCHAR(10),
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
