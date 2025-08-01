import React from 'react';

const navItems = [  
  { label: 'Users', key: 'users' },
  { label: 'Email', key: 'users' },
];

interface SideNavProps {
  selectedTab: string;
  onTabChange: (tab: string) => void;
}

const SideNav: React.FC<SideNavProps> = ({ selectedTab, onTabChange }) => (
  <nav className="w-56 min-w-56 max-w-56 bg-white border-r shadow flex flex-col py-8 ">
   {navItems.map((item) => (
      <button
        key={item.key}
        className={`px-6 py-3 text-left text-lg font-medium rounded transition mb-2 focus:outline-none focus:ring-2 focus:ring-blue-200 ${
          selectedTab === item.key
            ? 'bg-blue-100 text-blue-700 border-l-4 border-blue-400'
            : 'hover:bg-blue-50 text-gray-700'
        }`}
        onClick={() => onTabChange(item.key)}
      >
        {item.label}
      </button>
    ))}
  </nav>
);

export default SideNav;
