import { Injectable } from '@angular/core';
import { Apollo } from 'apollo-angular';
import { Router } from '@angular/router';
import { AUTH_STATUS_QUERY, LOGIN_MUTATION, LOGOUT_MUTATION, REGISTER_MUTATION } from '../graphql/mutations';
import { BehaviorSubject } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
  })
  export class AuthService {
    private isAuthenticated = new BehaviorSubject<boolean>(false);
  
    constructor(private apollo: Apollo, private router: Router) {
      // Vérifier l'état d'authentification au démarrage
      this.checkAuthStatus();
    }
  
    private checkAuthStatus() {
      // Appel GraphQL pour vérifier si l'utilisateur est authentifié
      this.apollo.query({
        query: AUTH_STATUS_QUERY
      }).subscribe({
        next: (result: any) => {
          this.isAuthenticated.next(result.data.isAuthenticated);
        },
        error: () => {
          this.isAuthenticated.next(false);
        }
      });
    }
  
    login(email: string, password: string) {
      return this.apollo.mutate({
        mutation: LOGIN_MUTATION,
        variables: { email, password },
        context: {
            // Important: permet d'envoyer les cookies
            withCredentials: true,
            headers: new HttpHeaders()
                .set('Content-Type', 'application/json')
                .set('Accept', 'application/json')
            }
      }).subscribe({
        
        next: (result: any) => {
          console.log(result);
          console.log('Cookies:', document);
          if (result.data.login) {
            this.isAuthenticated.next(true);
            this.router.navigate(['/']);
          }
        },
        error: (error) => {
          console.error('Login error:', error);
        }
      });
    }
  
    register(name: string, email: string, password: string) {
      return this.apollo.mutate({
        mutation: REGISTER_MUTATION,
        variables: { name, email, password },
      }).subscribe({
        next: (result: any) => {
          if (result.data.register.success) {
            this.isAuthenticated.next(true);
            this.router.navigate(['/']);
          }
        },
        error: (error) => {
          console.error('Register error:', error);
        }
      });
    }
  
    logout() {
      this.apollo.mutate({
        mutation: LOGOUT_MUTATION
      }).subscribe({
        next: () => {
          this.isAuthenticated.next(false);
          this.router.navigate(['/login']);
        }
      });
    }
  
    getAuthStatus() {
      return this.isAuthenticated.asObservable();
    }
  }