export type Users = {
  id: number;
  name: string;
  emailDate: string;
  subject: string;
  content: string;
  users: string;
  cc: string;
  bcc: string;
  template: string;
  status: 'draft' | 'sent' | 'failed' | 'scheduled';
};