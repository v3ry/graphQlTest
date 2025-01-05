
export interface User {
  id: string;
  email: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface LoginVariables {
  email: string;
  password: string;
}

export interface RegisterVariables {
  email: string;
  password: string;
}