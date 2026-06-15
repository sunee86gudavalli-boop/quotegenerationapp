import React from 'react';

function Header() {
  return (
    <header className="header">
      <div className="header-logo">
        <div>
          <div className="header-title">Bristol West Insurance</div>
          <div className="header-subtitle">A Farmers Insurance Company</div>
        </div>
      </div>
      <div style={{ color: '#a8c0e8', fontSize: '0.85rem', textAlign: 'right' }}>
        <div>1-800-BRISTOL</div>
        <div>bristolwest.com</div>
      </div>
    </header>
  );
}

export default Header;
