import React, { useState } from 'react';
import SideNav from './components/SideNav';
import Header from './pages/header';
import Footer from './pages/footer';
import Emails from './components/Emails';
import UserManagement from './components/UserManagement';

const App: React.FC = () => {
  const [tab, setTab] = useState<'users' | 'emails'>('users');
  return (
    <div className="w-full min-h-screen flex flex-col bg-gray-50">
      <Header />
      <div className="flex flex-1 min-h-0 ">
        <SideNav selectedTab={tab} onTabChange={tab => setTab(tab as 'users' | 'emails')} />
          <div className="flex-1 p-2 bg-gray-50">
            {tab === 'users' ?  <UserManagement /> :<Emails />}
          </div>
      </div>
      <Footer />
    </div>
  );
};

export default App;
