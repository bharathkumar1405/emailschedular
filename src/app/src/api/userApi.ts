import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:4000'; // Change if needed

export interface User {
  id: number;
  name: string;
  users: string;
  age: number;
}

export const getUsers = (page: number, limit: number) =>
  axios.get<User[]>('/users', {
    params: { _page: page, _limit: limit },
  });

export const addUser = (user: Omit<User, 'id'>) =>
  axios.post<User>('/users', user);

export const updateUser = (id: number, user: User) =>
  axios.put<User>(`/users/${id}`, user);

export const deleteUser = (id: number) =>
  axios.delete(`/users/${id}`);
