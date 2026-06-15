import React from 'react';

function FormField({ label, required, error, children }) {
  return (
    <div className="form-field">
      {label && (
        <label>
          {label}
          {required && <span className="required">*</span>}
        </label>
      )}
      {children}
      {error && <span className="error-msg">{error}</span>}
    </div>
  );
}

export default FormField;
