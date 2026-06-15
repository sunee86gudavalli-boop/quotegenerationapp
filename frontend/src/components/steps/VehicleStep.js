import React, { useState } from 'react';
import FormField from '../common/FormField';

const CURRENT_YEAR = new Date().getFullYear();
const YEARS = Array.from({ length: 30 }, (_, i) => CURRENT_YEAR - i);

function VehicleStep({ formData, onChange, onNext, onBack }) {
  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!formData.vehicleMake?.trim()) errs.vehicleMake = 'Make is required';
    if (!formData.vehicleModel?.trim()) errs.vehicleModel = 'Model is required';
    if (!formData.vehicleYear) errs.vehicleYear = 'Year is required';
    return errs;
  };

  const handleNext = () => {
    const errs = validate();
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    onNext();
  };

  const handleChange = (field, value) => {
    onChange(field, value);
    if (errors[field]) setErrors(prev => ({ ...prev, [field]: null }));
  };

  return (
    <div>
      <div className="form-section">
        <div className="section-title">Vehicle Information</div>
        <div className="form-row three-col">
          <FormField label="Year" required error={errors.vehicleYear}>
            <select
              value={formData.vehicleYear || ''}
              onChange={e => handleChange('vehicleYear', e.target.value)}
            >
              <option value="">Select Year</option>
              {YEARS.map(y => <option key={y} value={y}>{y}</option>)}
            </select>
          </FormField>
          <FormField label="Make" required error={errors.vehicleMake}>
            <input
              type="text"
              value={formData.vehicleMake || ''}
              onChange={e => handleChange('vehicleMake', e.target.value)}
              placeholder="Toyota"
            />
          </FormField>
          <FormField label="Model" required error={errors.vehicleModel}>
            <input
              type="text"
              value={formData.vehicleModel || ''}
              onChange={e => handleChange('vehicleModel', e.target.value)}
              placeholder="Camry"
            />
          </FormField>
        </div>
        <div className="form-row">
          <FormField label="VIN (optional)" error={errors.vin}>
            <input
              type="text"
              value={formData.vin || ''}
              onChange={e => handleChange('vin', e.target.value.toUpperCase())}
              placeholder="1HGCM82633A123456"
              maxLength={17}
            />
          </FormField>
          <FormField label="Primary Use" error={errors.usageType}>
            <select
              value={formData.usageType || 'PERSONAL'}
              onChange={e => handleChange('usageType', e.target.value)}
            >
              <option value="PERSONAL">Personal</option>
              <option value="COMMUTE">Commute to Work</option>
              <option value="BUSINESS">Business</option>
            </select>
          </FormField>
        </div>
        <div className="form-row">
          <FormField label="Annual Mileage" error={errors.annualMileage}>
            <input
              type="number"
              value={formData.annualMileage || ''}
              onChange={e => handleChange('annualMileage', e.target.value)}
              placeholder="12000"
              min={0}
              max={200000}
            />
          </FormField>
          <FormField label="Garaging ZIP Code" error={errors.garagingZip}>
            <input
              type="text"
              value={formData.garagingZip || ''}
              onChange={e => handleChange('garagingZip', e.target.value)}
              placeholder="33601"
              maxLength={10}
            />
          </FormField>
        </div>
      </div>

      <div className="btn-row">
        <button className="btn btn-secondary" onClick={onBack}>
          ← Back
        </button>
        <button className="btn btn-primary" onClick={handleNext}>
          Next: Driver Info →
        </button>
      </div>
    </div>
  );
}

export default VehicleStep;
