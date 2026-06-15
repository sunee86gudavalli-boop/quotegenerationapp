-- Create databases for each microservice
CREATE DATABASE IF NOT EXISTS bristolwest_customer;
CREATE DATABASE IF NOT EXISTS bristolwest_vehicle;
CREATE DATABASE IF NOT EXISTS bristolwest_driver;
CREATE DATABASE IF NOT EXISTS bristolwest_rating;
CREATE DATABASE IF NOT EXISTS bristolwest_quote;

-- Grant privileges to root on all DBs
GRANT ALL PRIVILEGES ON bristolwest_customer.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bristolwest_vehicle.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bristolwest_driver.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bristolwest_rating.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON bristolwest_quote.* TO 'root'@'%';
FLUSH PRIVILEGES;
