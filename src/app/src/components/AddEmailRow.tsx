import React, { useState } from 'react';
import { type Users } from '../types/Users';

const AddEmailRow = () => {
{isAdding && (
                  <tr className="bg-green-50">
                    <td className="px-2 py-2"></td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="name"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.name}
                        onChange={e => setNewEmail({ ...newEmail, name: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="datetime-local"
                        name="emailDate"
                        className="border rounded px-2 py-1"
                        value={newEmail.emailDate}
                        onChange={e => setNewEmail({ ...newEmail, emailDate: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="subject"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.subject}
                        onChange={e => setNewEmail({ ...newEmail, subject: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="content"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.content}
                        onChange={e => setNewEmail({ ...newEmail, content: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="users"
                        name="users"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.users}
                        onChange={e => setNewEmail({ ...newEmail, users: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="cc"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.cc}
                        onChange={e => setNewEmail({ ...newEmail, cc: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="bcc"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.bcc}
                        onChange={e => setNewEmail({ ...newEmail, bcc: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <input
                        type="text"
                        name="template"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.template}
                        onChange={e => setNewEmail({ ...newEmail, template: e.target.value })}
                      />
                    </td>
                    <td className="px-2 py-2">
                      <select
                        name="status"
                        className="border rounded px-2 py-1 w-full"
                        value={newEmail.status}
                        onChange={e => setNewEmail({ ...newEmail, status: e.target.value as Users['status'] })}
                      >
                        <option value="draft">Draft</option>
                        <option value="sent">Sent</option>
                        <option value="failed">Failed</option>
                        <option value="scheduled">Scheduled</option>
                      </select>
                    </td>
                    <td className="px-2 py-2 flex gap-2">
                      <button
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
                        onClick={handleAddEmail}
                      >
                        Add
                      </button>
                      <button
                        className="text-red-600 hover:text-red-800"
                        onClick={() => setIsAdding(false)}
                      >
                        Cancel
                      </button>
                    </td>
                  </tr>
                )}
            }