import React, { useState } from 'react';
import UserTable from './Emails';
import {type Users } from '../types/Users';

const initialUsers: Users[] = [
  {
    id: 1, name: 'John Doe', users: 'john@example.com',
    emailDate: '',
    subject: '',
    content: '',
    cc: '',
    bcc: '',
    template: '',
    status: 'draft'
  },
  {
    id: 2, name: 'Jane Smith', users: 'jane@example.com',
    emailDate: '',
    subject: '',
    content: '',
    cc: '',
    bcc: '',
    template: '',
    status: 'draft'
  },
];

const UserTableContainer: React.FC = () => {
  const [users, setUsers] = useState<Users[]>(initialUsers);
  const [addUser, setAddUser] = useState<Omit<Users, 'id'>>({
    name: '',
    users: '',
    emailDate: '',
    subject: '',
    content: '',
    cc: '',
    bcc: '',
    template: '',
    status: 'draft'
  });

  const handleAdd = () => {
    if (!addUser.name || !addUser.users ) return;
    setUsers([
      ...users,
      { ...addUser, id: users.length ? Math.max(...users.map(u => u.id)) + 1 : 1 },
    ]);
    setAddUser({ name: '', users: '', emailDate: '', subject: '', content: '', cc: '', bcc: '', template: '', status: 'draft' });
  };

  return (
    <div className="max-w-4xl mx-auto py-8 px-2">
      <h3 className="mb-4 text-2xl font-bold text-center">User Management</h3>
      <div className="bg-white rounded shadow p-4 mb-6">
        <div className="flex flex-col md:flex-row gap-2 md:gap-4 items-center">
          <input
            type="text"
            className="border rounded px-3 py-2 flex-1"
            placeholder="Name"
            name="name"
            id ="name"
            value={addUser.name}
            onChange={e => setAddUser(f => ({ ...f, name: e.target.value }))}
          />
          <input
            type="users"
            className="border rounded px-3 py-2 flex-1"
            placeholder="Email"
            name="users"
            id ="users"
            value={addUser.users}
            onChange={e => setAddUser(f => ({ ...f, users: e.target.value }))}
          />
          <input
            type="number"
            className="border rounded px-3 py-2 w-24"
            placeholder="template"
            name="template"
            id="template"
            value={addUser.template || ''}
            min={0}
            onChange={e => setAddUser(f => ({ ...f, age: Number(e.target.value) }))}
          />
          <button className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded md:w-auto" onClick={handleAdd}>Add</button>
        </div>
      </div>
      <div className="bg-white rounded shadow p-2 overflow-x-auto">
        <UserTable />
      </div>
    </div>
  );
};

export default UserTableContainer;
