import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario = '';
  contrasena = '';
  error = '';
  cargando = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (!this.usuario || !this.contrasena) {
      this.error = 'Por favor completa todos los campos';
      return;
    }

    this.cargando = true;
    this.error = '';

    this.authService.login(this.usuario, this.contrasena).subscribe(
      (response) => {
        localStorage.setItem('usuario', JSON.stringify(response));
        this.router.navigate(['/tickets']);
        this.cargando = false;
      },
      (error) => {
        this.error = error.error || 'Usuario o contrasena incorrectos';
        this.cargando = false;
      }
    );
  }
}

