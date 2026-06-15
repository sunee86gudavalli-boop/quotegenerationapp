import React, { useState } from 'react';
import FormField from '../common/FormField';

const US_STATES = [
  'AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA',
  'KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ',
  'NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT',
  'VA','WA','WV','WI','WY'
];

function CustomerStep({ formData, onChange, onNext }) {
  const [errors, setErrors] = useState({});

  const validate = () => {
    const errs = {};
    if (!formData.firstName?.trim()) errs.firstName = 'First name is required';
    if (!formData.lastName?.trim()) errs.lastName = 'Last name is required';
    if (!formData.email?.trim()) errs.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) errs.email = 'Invalid email format';
    if (!formData.dateOfBirth) errs.dateOfBirth = 'Date of birth is required';
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
        <div className="section-title">Personal Information</div>
        <div className="form-row">
          <FormField label="First Name" required error={errors.firstName}>
            <input
              type="text"
              value={formData.firstName || ''}
              onChange={e => handleChange('firstName', e.target.value)}
              placeholder="John"
            />
          </FormField>
          <FormField label="Last Name" required error={errors.lastName}>
            <input
              type="text"
              value={formData.lastName || ''}
              onChange={e => handleChange('lastName', e.target.value)}
              placeholder="Doe"
            />
          </FormField>
        </div>
        <div className="form-row">
          <FormField label="Email Address" required error={errors.email}>
            <input
              type="email"
              value={formData.email || ''}
              onChange={e => handleChange('email', e.target.value)}
              placeholder="john.doe@email.com"
            />
          </FormField>
          <FormField label="Phone Number" error={errors.phone}>
            <input
              type="tel"
              value={formData.phone || ''}
              onChange={e => handleChange('phone', e.target.value)}
              placeholder="(555) 555-5555"
            />
          </FormField>
        </div>
        <div className="form-row">
          <FormField label="Date of Birth" required error={errors.dateOfBirth}>
            <input
              type="date"
              value={formData.dateOfBirth || ''}
              onChange={e => handleChange('dateOfBirth', e.target.value)}
              max={new Date().toISOString().split('T')[0]}
            />
          </FormField>
        </div>
      </div>

      <div className="form-section">
        <div className="section-title">Mailing Address</div>
        <div className="form-row single">
          <FormField label="Street Address" error={errors.address}>
            <input
              type="text"
              value={formData.address || ''}
              onChange={e => handleChange('address', e.target.value)}
              placeholder="123 Main Street"
            />
          </FormField>
        </div>
        <div className="form-row three-col">
          <FormField label="City" error={errors.city}>
            <input
              type="text"
              value={formData.city || ''}
              onChange={e => handleChange('city', e.target.value)}
              placeholder="Tampa"
            />
          </FormField>
          <FormField label="State" error={errors.state}>
            <select
              value={formData.state || ''}
              onChange={e => handleChange('state', e.target.value)}
            >
              <option value="">Select State</option>
              {US_STATES.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
          </FormField>
          <FormField label="ZIP Code" error={errors.zipCode}>
            <input
              type="text"
              value={formData.zipCode || ''}
              onChange={e => handleChange('zipCode', e.target.value)}
              placeholder="33601"
              maxLength={10}
            />
          </FormField>
        </div>
      </div>

      <div className="btn-row">
        <div />
        <button className="btn btn-primary" onClick={handleNext}>
          Next: Vehicle Info →
        </button>
      </div>
    </div>
  );
}

export default CustomerStep;
