import React, { useState } from 'react';
import ProgressBar from './common/ProgressBar';
import CustomerStep from './steps/CustomerStep';
import VehicleStep from './steps/VehicleStep';
import DriverStep from './steps/DriverStep';
import CoverageStep from './steps/CoverageStep';
import QuoteSummary from './steps/QuoteSummary';
import { generateQuote } from '../services/quoteService';

const STEP_TITLES = {
  1: { title: 'Your Information', subtitle: 'Tell us about yourself' },
  2: { title: 'Vehicle Information', subtitle: 'What would you like to insure?' },
  3: { title: 'Driver Information', subtitle: 'Tell us about your driving history' },
  4: { title: 'Coverage Selection', subtitle: 'Choose the right coverage for you' },
  5: { title: 'Your Quote', subtitle: 'Your personalized auto insurance quote' },
};

const INITIAL_FORM = {
  // Customer
  firstName: '', lastName: '', email: '', phone: '',
  dateOfBirth: '', address: '', city: '', state: '', zipCode: '',
  // Vehicle
  vin: '', vehicleMake: '', vehicleModel: '', vehicleYear: '',
  usageType: 'PERSONAL', annualMileage: '', garagingZip: '',
  // Driver
  licenseNumber: '', licenseState: '', driverDateOfBirth: '',
  yearsLicensed: '', violationsCount: '0', accidentsCount: '0',
  // Coverage
  coverageType: '',
};

function QuoteWizard() {
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [quote, setQuote] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleNext = () => setStep(prev => prev + 1);
  const handleBack = () => setStep(prev => prev - 1);

  const handleSubmitQuote = async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await generateQuote(formData);
      setQuote(result);
      setStep(5);
    } catch (err) {
      const msg = err.response?.data?.error
        || err.response?.data?.message
        || 'Failed to generate quote. Please try again.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const handleStartOver = () => {
    setStep(1);
    setFormData(INITIAL_FORM);
    setQuote(null);
    setError(null);
  };

  const { title, subtitle } = STEP_TITLES[step];

  return (
    <div className="wizard-container">
      <div className="wizard-header">
        <h2>{title}</h2>
        <p>{subtitle}</p>
      </div>

      <div className="wizard-body">
        {step < 5 && <ProgressBar currentStep={step} />}

        {error && <div className="error-banner">{error}</div>}

        {loading ? (
          <div className="loading-overlay">
            <div className="spinner" />
            <p style={{ color: '#003087', fontWeight: 600 }}>Calculating your premium...</p>
            <p style={{ color: '#888', fontSize: '0.85rem' }}>
              Connecting to our rating engine. This may take a moment.
            </p>
          </div>
        ) : (
          <>
            {step === 1 && (
              <CustomerStep
                formData={formData}
                onChange={handleChange}
                onNext={handleNext}
              />
            )}
            {step === 2 && (
              <VehicleStep
                formData={formData}
                onChange={handleChange}
                onNext={handleNext}
                onBack={handleBack}
              />
            )}
            {step === 3 && (
              <DriverStep
                formData={formData}
                onChange={handleChange}
                onNext={handleNext}
                onBack={handleBack}
              />
            )}
            {step === 4 && (
              <CoverageStep
                formData={formData}
                onChange={handleChange}
                onNext={handleSubmitQuote}
                onBack={handleBack}
              />
            )}
            {step === 5 && (
              <QuoteSummary
                quote={quote}
                formData={formData}
                onStartOver={handleStartOver}
              />
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default QuoteWizard;
