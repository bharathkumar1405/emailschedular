import React, { useState } from 'react';
import { type Users } from '../types/Users';

const UserManagement = () => {
  const [emails, setEmails] = useState<Users[]>([
    {
      id: 1,
      name: 'John Doe',
      emailDate: '2023-05-15T10:30:00',
      subject: 'Quarterly Report',
      content: 'Please find attached the quarterly report...',
      users: 'john@example.com',
      cc: 'team@example.com',
      bcc: 'manager@example.com',
      template: 'report-template',
      status: 'sent'
    },
    {
      id: 2,
      name: 'Jane Smith',
      emailDate: '2023-05-16T14:45:00',
      subject: 'Meeting Reminder',
      content: 'This is a reminder about our meeting tomorrow...',
      users: 'jane@example.com',
      cc: '',
      bcc: '',
      template: 'reminder-template',
      status: 'scheduled'
    }
  ]);

  const [isAdding, setIsAdding] = useState(false);
  const [newEmail, setNewEmail] = useState<Omit<Users, 'id'>>({
    name: '',
    emailDate: new Date().toISOString().slice(0, 16),
    subject: '',
    content: '',
    users: '',
    cc: '',
    bcc: '',
    template: '',
    status: 'draft'
  });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editEmail, setEditEmail] = useState<Partial<Users>>({});

  const handleAddEmail = () => {
    if (!newEmail.name || !newEmail.users || !newEmail.subject) return;
    const users: Users = {
      ...newEmail,
      id: emails.length ? Math.max(...emails.map(e => Number(e.id))) + 1 : 1
    };
    setEmails([...emails, users]);
    setNewEmail({
      name: '',
      emailDate: new Date().toISOString().slice(0, 16),
      subject: '',
      content: '',
      users: '',
      cc: '',
      bcc: '',
      template: '',
      status: 'draft'
    });
    setIsAdding(false);
  };

  const handleDeleteEmail = (id: number) => {
    setEmails(emails.filter(users => users.id !== id));
  };

  const handleEditStart = (users: Users) => {
    setEditingId(users.id);
    setEditEmail({ ...users });
  };

  const handleEditChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setEditEmail({ ...editEmail, [e.target.name]: e.target.value });
  };

  const handleEditSave = () => {
    setEmails(emails.map(users => (users.id === editingId ? { ...users, ...editEmail } as Users : users)));
    setEditingId(null);
    setEditEmail({});
  };

  const handleEditCancel = () => {
    setEditingId(null);
    setEditEmail({});
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'draft': return 'bg-yellow-100 text-yellow-800';
      case 'sent': return 'bg-green-100 text-green-800';
      case 'failed': return 'bg-red-100 text-red-800';
      case 'scheduled': return 'bg-blue-100 text-blue-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
      <div className="bg-white rounded-lg shadow p-2 w-full max-w-full overflow-x-auto">
        <div className="flex flex-col md:flex-row md:justify-between md:items-center mb-6">
          <h1 className="text-2xl font-bold mb-4 md:mb-0">Email Management</h1>
          <button
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center"
            onClick={() => setIsAdding(true)}
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Add Email
          </button>
        </div>
        <div className="overflow-x-auto w-full max-w-full">
          <table className="w-full bg-white border border-gray-200 rounded-xl shadow-lg text-xs table-fixed">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-2 py-2 text-left font-semibold rounded-tl-xl w-12">ID</th>
                <th className="px-2 py-2 text-left font-semibold w-24">Name</th>
                <th className="px-2 py-2 text-left font-semibold w-32">Email Date</th>
                <th className="px-2 py-2 text-left font-semibold w-32">Subject</th>
                <th className="px-2 py-2 text-left font-semibold w-40">Content</th>
                <th className="px-2 py-2 text-left font-semibold w-32">Email</th>
                <th className="px-2 py-2 text-left font-semibold w-20">CC</th>
                <th className="px-2 py-2 text-left font-semibold w-20">BCC</th>
                <th className="px-2 py-2 text-left font-semibold w-24">Template</th>
                <th className="px-2 py-2 text-left font-semibold w-20">Status</th>
                <th className="px-2 py-2 text-left font-semibold rounded-tr-xl w-24">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
                  {isAdding && (
                  <AddEmailRow
                    newEmail={newEmail}
                    setNewEmail={setNewEmail}
                    onAdd={handleAddEmail}
                    onCancel={() => setIsAdding(false)}
                  />
                  )}
              {emails.map((users) =>
                editingId === users.id ? (
                  <tr key={users.id} className="bg-yellow-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{users.id}</td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="text"
                        name="name"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.name || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="datetime-local"
                        name="emailDate"
                        className="border rounded px-2 py-1"
                        value={editEmail.emailDate || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="text"
                        name="subject"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.subject || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4">
                      <input
                        type="text"
                        name="content"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.content || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="users"
                        name="users"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.users || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="text"
                        name="cc"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.cc || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="text"
                        name="bcc"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.bcc || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <input
                        type="text"
                        name="template"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.template || ''}
                        onChange={handleEditChange}
                      />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <select
                        name="status"
                        className="border rounded px-2 py-1 w-full"
                        value={editEmail.status || ''}
                        onChange={handleEditChange}
                      >
                        <option value="draft">Draft</option>
                        <option value="sent">Sent</option>
                        <option value="failed">Failed</option>
                        <option value="scheduled">Scheduled</option>
                      </select>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap flex gap-2">
                      <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
                        onClick={handleEditSave}
                      >
                        Save
                      </button>
                      <button
                        className="text-red-600 hover:text-red-800"
                        onClick={handleEditCancel}
                      >
                        Cancel
                      </button>
                    </td>
                  </tr>
                ) : (
                  <tr key={users.id}>
                    <td className="px-2 py-2 whitespace-normal break-words text-sm text-gray-500">{users.id}</td>
                    <td className="px-2 py-2 whitespace-normal break-words">{users.name}</td>
                    <td className="px-2 py-2 whitespace-normal break-words text-sm">{formatDate(users.emailDate)}</td>
                    <td className="px-2 py-2 whitespace-normal break-words max-w-xs">{users.subject}</td>
                    <td className="px-2 py-2 whitespace-normal break-words max-w-xs">{users.content}</td>
                    <td className="px-2 py-2 whitespace-normal break-words">{users.users}</td>
                    <td className="px-2 py-2 whitespace-normal break-words text-sm">{users.cc || '-'}</td>
                    <td className="px-2 py-2 whitespace-normal break-words text-sm">{users.bcc || '-'}</td>
                    <td className="px-2 py-2 whitespace-normal break-words text-sm">{users.template || '-'}</td>
                    <td className="px-2 py-2 whitespace-normal break-words">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(users.status)}`}>
                        {users.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap flex gap-2">
                      <button
                        className="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded-full"
                        onClick={() => handleEditStart(users)}
                      >
                        Edit
                      </button>
                      <button
                        className="text-red-600 hover:text-red-800"
                        onClick={() => handleDeleteEmail(users.id)}
                      >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </td>
                  </tr>
                )
              )}
            </tbody>
          </table>
        </div>
        {emails.length === 0 && (
          <div className="text-center py-8 text-gray-500">
            No emails found. Add an users to get started.
          </div>
        )}
      </div>
  );
};

export default UserManagement;