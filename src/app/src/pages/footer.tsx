import React from 'react';

const Footer: React.FC = () => (
  <footer className="w-full text-center py-4 bg-gray-100 text-gray-600 mt-auto border-t">
    &copy; {new Date().getFullYear()} Infomerica. All rights reserved.
  </footer>
);

export default Footer;