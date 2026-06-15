import React from 'react';

const STEPS = [
  { label: 'Customer' },
  { label: 'Vehicle' },
  { label: 'Driver' },
  { label: 'Coverage' },
  { label: 'Quote' },
];

function ProgressBar({ currentStep }) {
  return (
    <div className="progress-bar">
      {STEPS.map((step, index) => {
        const stepNum = index + 1;
        const isCompleted = stepNum < currentStep;
        const isActive = stepNum === currentStep;

        return (
          <div
            key={stepNum}
            className={`progress-step ${isActive ? 'active' : ''} ${isCompleted ? 'completed' : ''}`}
          >
            <div className="step-circle">
              {isCompleted ? '✓' : stepNum}
            </div>
            <div className="step-label">{step.label}</div>
          </div>
        );
      })}
    </div>
  );
}

export default ProgressBar;
