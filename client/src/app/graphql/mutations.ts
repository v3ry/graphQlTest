
import { gql } from 'apollo-angular';

export const LOGIN_MUTATION = gql`
  mutation Login($email: String!, $password: String!) {
    login(email: $email, password: $password) {
        id
        email
    }
    
  }
`;

export const REGISTER_MUTATION = gql`
  mutation Register($name: String!, $email: String!, $password: String!) {
    register(name: $name, email: $email, password: $password) {
        id
        name
        email
        roles
    }
  }
  
`;

export const LOGOUT_MUTATION = gql`
  mutation Logout {
    logout
  }
`;

export const AUTH_STATUS_QUERY = gql`
  query AuthStatus {
    isAuthenticated
  }
`;