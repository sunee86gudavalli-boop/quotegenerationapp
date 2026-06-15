import React from 'react';

function formatCurrency(amount) {
  if (!amount) return '$0.00';
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
}

function formatDate(dateStr) {
  if (!dateStr) return 'N/A';
  return new Date(dateStr).toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric'
  });
}

const COVERAGE_LABELS = {
  LIABILITY: 'Liability Only',
  STANDARD: 'Standard Coverage',
  FULL: 'Full Coverage',
};

function QuoteSummary({ quote, formData, onStartOver }) {
  if (!quote) return null;

  const showCollision = ['STANDARD', 'FULL'].includes(quote.coverageType);
  const showComprehensive = quote.coverageType === 'FULL';

  return (
    <div className="quote-summary">
      <div className="quote-success">
        <div className="quote-success-icon">🎉</div>
        <h2>Your Quote is Ready!</h2>
        <p style={{ color: '#666', marginBottom: '0.5rem' }}>
          Congratulations, {quote.customerName || formData.firstName + ' ' + formData.lastName}!
        </p>
        <div className="quote-number">{quote.quoteNumber}</div>
      </div>

      <div className="premium-breakdown">
        <h3>Premium Breakdown — Annual</h3>

        <div className="premium-line">
          <span>Base Premium</span>
          <span>{formatCurrency(quote.basePremium)}</span>
        </div>
        <div className="premium-line">
          <span>Liability Coverage</span>
          <span>{formatCurrency(quote.liabilityPremium)}</span>
        </div>
        {showCollision && (
          <div className="premium-line">
            <span>Collision Coverage</span>
            <span>{formatCurrency(quote.collisionPremium)}</span>
          </div>
        )}
        {showComprehensive && (
          <div className="premium-line">
            <span>Comprehensive Coverage</span>
            <span>{formatCurrency(quote.comprehensivePremium)}</span>
          </div>
        )}

        <div className="premium-total">
          <span>Total Annual Premium</span>
          <span>{formatCurrency(quote.totalPremium)}</span>
        </div>
      </div>

      <div className="quote-meta">
        <div className="meta-card">
          <div className="meta-label">Coverage Type</div>
          <div className="meta-value">{COVERAGE_LABELS[quote.coverageType] || quote.coverageType}</div>
        </div>
        <div className="meta-card">
          <div className="meta-label">Vehicle</div>
          <div className="meta-value">
            {quote.vehicleYear} {quote.vehicleMakeModel || `${formData.vehicleMake} ${formData.vehicleModel}`}
          </div>
        </div>
        <div className="meta-card">
          <div className="meta-label">Quote Date</div>
          <div className="meta-value">{formatDate(quote.createdAt)}</div>
        </div>
        <div className="meta-card">
          <div className="meta-label">Valid Until</div>
          <div className="meta-value">{formatDate(quote.expiresAt)}</div>
        </div>
      </div>

      <div className="email-notice">
        A copy of this quote has been sent to <strong>{quote.customerEmail || formData.email}</strong>
      </div>

      <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', marginTop: '1.5rem', flexWrap: 'wrap' }}>
        <button
          className="btn btn-primary"
          onClick={() => window.print()}
        >
          Print Quote
        </button>
        <button
          className="btn btn-secondary"
          onClick={onStartOver}
        >
          Start New Quote
        </button>
      </div>

      <div style={{ marginTop: '1.5rem', padding: '1rem', background: '#f5f7fa', borderRadius: '8px', fontSize: '0.82rem', color: '#888' }}>
        To convert this quote to a policy, call <strong>1-800-BRISTOL</strong> or visit your local Farmers agent.
        Quote #{quote.quoteNumber} is valid for 30 days.
      </div>
    </div>
  );
}

export default QuoteSummary;
