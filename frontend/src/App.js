import React from 'react';
import Header from './components/common/Header';
import QuoteWizard from './components/QuoteWizard';

function App() {
  return (
    <div className="app">
      <Header />
      <main className="main-content">
        <QuoteWizard />
      </main>
      <footer className="footer">
        <p>© 2026 Bristol West Insurance Group. A Farmers Insurance Company. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;
