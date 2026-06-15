import React, { useState } from 'react';
import FormField from '../common/FormField';

const US_STATES = [
  'AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA',
  'KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ',
  'NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT',
  'VA','WA','WV','WI','WY'
];

function DriverStep({ formData, onChange, onNext, onBack }) {
  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!formData.licenseNumber?.trim()) errs.licenseNumber = 'License number is required';
    if (!formData.licenseState) errs.licenseState = 'License state is required';
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
        <div className="section-title">Primary Driver Information</div>
        <div className="form-row">
          <FormField label="Driver's License Number" required error={errors.licenseNumber}>
            <input
              type="text"
              value={formData.licenseNumber || ''}
              onChange={e => handleChange('licenseNumber', e.target.value.toUpperCase())}
              placeholder="FL123456789"
            />
          </FormField>
          <FormField label="License State" required error={errors.licenseState}>
            <select
              value={formData.licenseState || ''}
              onChange={e => handleChange('licenseState', e.target.value)}
            >
              <option value="">Select State</option>
              {US_STATES.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
          </FormField>
        </div>
        <div className="form-row">
          <FormField label="Driver Date of Birth" error={errors.driverDateOfBirth}>
            <input
              type="date"
              value={formData.driverDateOfBirth || formData.dateOfBirth || ''}
              onChange={e => handleChange('driverDateOfBirth', e.target.value)}
              max={new Date().toISOString().split('T')[0]}
            />
          </FormField>
          <FormField label="Years Licensed" error={errors.yearsLicensed}>
            <input
              type="number"
              value={formData.yearsLicensed || ''}
              onChange={e => handleChange('yearsLicensed', e.target.value)}
              placeholder="5"
              min={0}
              max={80}
            />
          </FormField>
        </div>
      </div>

      <div className="form-section">
        <div className="section-title">Driving History (last 3 years)</div>
        <div className="form-row">
          <FormField label="Traffic Violations" error={errors.violationsCount}>
            <select
              value={formData.violationsCount || '0'}
              onChange={e => handleChange('violationsCount', e.target.value)}
            >
              {[0,1,2,3,4,5].map(n => (
                <option key={n} value={n}>{n} {n === 0 ? '(None)' : n === 1 ? 'violation' : 'violations'}</option>
              ))}
            </select>
          </FormField>
          <FormField label="At-Fault Accidents" error={errors.accidentsCount}>
            <select
              value={formData.accidentsCount || '0'}
              onChange={e => handleChange('accidentsCount', e.target.value)}
            >
              {[0,1,2,3,4,5].map(n => (
                <option key={n} value={n}>{n} {n === 0 ? '(None)' : n === 1 ? 'accident' : 'accidents'}</option>
              ))}
            </select>
          </FormField>
        </div>
        {(parseInt(formData.violationsCount) > 0 || parseInt(formData.accidentsCount) > 0) && (
          <div className="error-banner">
            Note: Violations and accidents may result in premium surcharges.
          </div>
        )}
      </div>

      <div className="btn-row">
        <button className="btn btn-secondary" onClick={onBack}>
          ← Back
        </button>
        <button className="btn btn-primary" onClick={handleNext}>
          Next: Coverage →
        </button>
      </div>
    </div>
  );
}

export default DriverStep;
