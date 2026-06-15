DROP PROCEDURE IF EXISTS calculate_premium;

CREATE PROCEDURE calculate_premium(
  IN  p_driver_age            INT,
  IN  p_violations            INT,
  IN  p_accidents             INT,
  IN  p_vehicle_year          INT,
  IN  p_annual_mileage        INT,
  IN  p_coverage_type         VARCHAR(20),
  IN  p_zip_code              VARCHAR(10),
  OUT p_base_premium          DECIMAL(10,2),
  OUT p_liability_premium     DECIMAL(10,2),
  OUT p_collision_premium     DECIMAL(10,2),
  OUT p_comprehensive_premium DECIMAL(10,2),
  OUT p_total_premium         DECIMAL(10,2)
)
BEGIN
  SET p_base_premium = 800.00;

  -- Age surcharge
  IF p_driver_age < 25 THEN
    SET p_base_premium = p_base_premium * 1.40;
  ELSEIF p_driver_age > 70 THEN
    SET p_base_premium = p_base_premium * 1.20;
  END IF;

  -- Violations and accidents
  SET p_base_premium = p_base_premium + (p_violations * 150.00);
  SET p_base_premium = p_base_premium + (p_accidents  * 200.00);

  -- Older vehicle discount
  IF p_vehicle_year < (YEAR(CURDATE()) - 10) THEN
    SET p_base_premium = p_base_premium * 0.90;
  END IF;

  -- High mileage surcharge
  IF p_annual_mileage > 15000 THEN
    SET p_base_premium = p_base_premium * 1.10;
  END IF;

  -- Coverage breakdown
  SET p_liability_premium     = p_base_premium * 0.40;
  SET p_collision_premium     = 0.00;
  SET p_comprehensive_premium = 0.00;

  IF p_coverage_type = 'STANDARD' THEN
    SET p_collision_premium = p_base_premium * 0.35;
  ELSEIF p_coverage_type = 'FULL' THEN
    SET p_collision_premium     = p_base_premium * 0.35;
    SET p_comprehensive_premium = p_base_premium * 0.25;
  END IF;

  SET p_total_premium = p_liability_premium + p_collision_premium + p_comprehensive_premium;
END;
