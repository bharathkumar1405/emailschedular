import React, { useState } from 'react';

export interface User {
  id: number;
  name: string;
  users: string;
  age: number;
}

const initialUsers: User[] = [
  { id: 1, name: 'John Doe', users: 'john@example.com', age: 28 },
  { id: 2, name: 'Jane Smith', users: 'jane@example.com', age: 32 },
];

const Emails: React.FC = () => {
  const [users, setUsers] = useState<User[]>(initialUsers);
  const [isAdding, setIsAdding] = useState(false);
  const [newUser, setNewUser] = useState<Omit<User, 'id'>>({ name: '', users: '', age: 0 });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editUser, setEditUser] = useState<Partial<User>>({});

  const handleAddUser = () => {
    if (!newUser.name || !newUser.users || !newUser.age) return;
    setUsers([
      ...users,
      { ...newUser, id: users.length ? Math.max(...users.map(u => u.id)) + 1 : 1 }
    ]);
    setNewUser({ name: '', users: '', age: 0 });
    setIsAdding(false);
  };

  const handleEditStart = (user: User) => {
    setEditingId(user.id);
    setEditUser({ ...user });
  };
  const handleEditChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEditUser({ ...editUser, [e.target.name]: e.target.value });
  };
  const handleEditSave = () => {
    setUsers(users.map(u => (u.id === editingId ? { ...u, ...editUser, age: Number(editUser.age) } as User : u)));
    setEditingId(null);
    setEditUser({});
  };
  const handleEditCancel = () => {
    setEditingId(null);
    setEditUser({});
  };
  const handleDelete = (id: number) => {
    setUsers(users.filter(u => u.id !== id));
  };

  return (
    <div className="overflow-x-auto w-full">
            <div className="mt-4 flex justify-end">
        {!isAdding && (
          <button className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow" onClick={() => setIsAdding(true)}>
            + Add User
          </button>
        )}
      </div>
      <table className="min-w-full bg-white border border-gray-200 rounded-xl shadow-lg">
        <thead className="bg-blue-400 text-white">
          <tr>
            <th className="px-6 py-3 text-left text-sm font-semibold rounded-tl-xl">ID</th>
            <th className="px-6 py-3 text-left text-sm font-semibold">Name</th>
            <th className="px-6 py-3 text-left text-sm font-semibold">Email</th>
            <th className="px-6 py-3 text-left text-sm font-semibold">Age</th>
            <th className="px-6 py-3 text-left text-sm font-semibold rounded-tr-xl">Actions</th>
          </tr>
        </thead>
        <tbody>
          {isAdding && (
            <tr className="bg-blue-50 animate-pulse">
              <td className="px-6 py-2 text-gray-400 font-mono">New</td>
              <td className="px-6 py-2">
                <input
                  name="name"
                  className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-blue-400"
                  value={newUser.name}
                  onChange={e => setNewUser({ ...newUser, name: e.target.value })}
                  placeholder="Name"
                />
              </td>
              <td className="px-6 py-2">
                <input
                  name="users"
                  className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-blue-400"
                  value={newUser.users}
                  onChange={e => setNewUser({ ...newUser, users: e.target.value })}
                  placeholder="Email"
                  type="users"
                />
              </td>
              <td className="px-6 py-2">
                <input
                  name="age"
                  className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-blue-400"
                  value={newUser.age || ''}
                  onChange={e => setNewUser({ ...newUser, age: Number(e.target.value) })}
                  placeholder="Age"
                  type="number"
                  min={0}
                />
              </td>
              <td className="px-6 py-2 flex gap-2">
                <button className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded shadow" onClick={handleAddUser}>Add</button>
                <button className="bg-gray-300 hover:bg-gray-400 text-gray-700 px-3 py-1 rounded shadow" onClick={() => setIsAdding(false)}>Cancel</button>
              </td>
            </tr>
          )}
          {users.map(user =>
            editingId === user.id ? (
              <tr key={user.id} className="bg-yellow-50">
                <td className="px-6 py-2 font-mono text-gray-500">{user.id}</td>
                <td className="px-6 py-2">
                  <input
                    name="name"
                    className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-yellow-400"
                    value={editUser.name || ''}
                    onChange={handleEditChange}
                  />
                </td>
                <td className="px-6 py-2">
                  <input
                    name="users"
                    className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-yellow-400"
                    value={editUser.users || ''}
                    onChange={handleEditChange}
                  />
                </td>
                <td className="px-6 py-2">
                  <input
                    name="age"
                    className="border rounded px-2 py-1 w-full focus:ring-2 focus:ring-yellow-400"
                    value={editUser.age || ''}
                    onChange={handleEditChange}
                    type="number"
                    min={0}
                  />
                </td>
                <td className="px-6 py-2 flex gap-2">
                  <button className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded shadow" onClick={handleEditSave}>Save</button>
                  <button className="bg-gray-300 hover:bg-gray-400 text-gray-700 px-3 py-1 rounded shadow" onClick={handleEditCancel}>Cancel</button>
                </td>
              </tr>
            ) : (
              <tr key={user.id} className="hover:bg-gray-50 transition">
                <td className="px-6 py-2 font-mono text-gray-500">{user.id}</td>
                <td className="px-6 py-2 font-semibold text-gray-800">{user.name}</td>
                <td className="px-6 py-2 text-blue-700 underline">{user.users}</td>
                <td className="px-6 py-2">{user.age}</td>
                <td className="px-6 py-2 flex gap-2">
                  <button className="bg-yellow-400 hover:bg-yellow-500 text-white px-3 py-1 rounded shadow" onClick={() => handleEditStart(user)}>Edit</button>
                  <button className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded shadow" onClick={() => handleDelete(user.id)}>Delete</button>
                </td>
              </tr>
            )
          )}
        </tbody>
      </table>

    </div>
  );
};

export default Emails;
