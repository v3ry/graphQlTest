import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Importation de FormsModule
import { CommonModule } from '@angular/common'; // Importation de CommonModule
import { AuthService } from '../../services/auth.service';
import { Apollo } from 'apollo-angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true, // Déclaration en tant que composant standalone
  imports: [FormsModule, CommonModule] // Ajout de FormsModule et CommonModule aux imports
})
export class LoginComponent {
  name: string = '';
  email: string = '';
  password: string = '';
  loading: boolean = false;
  error: string | null = null;

  constructor(private authService: AuthService, private readonly apollo: Apollo) {}

  onSubmit() {
    this.loading = true;
    this.error = null;
    this.authService.login(this.email, this.password);
  }
}