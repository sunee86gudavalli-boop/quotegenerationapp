import React, { useState } from 'react';

const COVERAGE_OPTIONS = [
  {
    value: 'LIABILITY',
    label: 'Liability Only',
    description: 'Covers damages to others when you are at fault. Meets Florida state minimum requirements.',
    includes: ['Bodily Injury Liability', 'Property Damage Liability'],
  },
  {
    value: 'STANDARD',
    label: 'Standard Coverage',
    badge: 'Popular',
    description: 'Adds Collision coverage to protect your vehicle in accidents, regardless of fault.',
    includes: ['All Liability coverage', 'Collision Coverage', 'Uninsured Motorist'],
  },
  {
    value: 'FULL',
    label: 'Full Coverage',
    badge: 'Best Value',
    description: 'Comprehensive protection including theft, weather, and all collision scenarios.',
    includes: ['All Standard coverage', 'Comprehensive Coverage', 'Rental Reimbursement', 'Roadside Assistance'],
  },
];

function CoverageStep({ formData, onChange, onNext, onBack }) {
  const [error, setError] = useState('');

  const handleSelect = (value) => {
    onChange('coverageType', value);
    setError('');
  };

  const handleNext = () => {
    if (!formData.coverageType) {
      setError('Please select a coverage type to continue');
      return;
    }
    onNext();
  };

  return (
    <div>
      <div className="form-section">
        <div className="section-title">Select Your Coverage Level</div>
        {error && <div className="error-banner">{error}</div>}
        <div className="coverage-options">
          {COVERAGE_OPTIONS.map(option => (
            <div
              key={option.value}
              className={`coverage-card ${formData.coverageType === option.value ? 'selected' : ''}`}
              onClick={() => handleSelect(option.value)}
            >
              <input
                type="radio"
                name="coverage"
                value={option.value}
                checked={formData.coverageType === option.value}
                onChange={() => handleSelect(option.value)}
              />
              <div className="coverage-info">
                <h4>
                  {option.label}
                  {option.badge && <span className="coverage-badge">{option.badge}</span>}
                </h4>
                <p>{option.description}</p>
                <ul style={{ marginTop: '0.5rem', paddingLeft: '1.2rem', fontSize: '0.82rem', color: '#555' }}>
                  {option.includes.map(item => <li key={item}>{item}</li>)}
                </ul>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="btn-row">
        <button className="btn btn-secondary" onClick={onBack}>
          ← Back
        </button>
        <button className="btn btn-primary" onClick={handleNext}>
          Get My Quote →
        </button>
      </div>
    </div>
  );
}

export default CoverageStep;
