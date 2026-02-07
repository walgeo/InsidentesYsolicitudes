import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-box">
        <h2>Iniciar Sesión</h2>
        
        <form (ngSubmit)="login()" class="login-form">
          <div class="form-group">
            <label for="usuario">Usuario:</label>
            <input 
              type="text" 
              id="usuario" 
              [(ngModel)]="usuario" 
              name="usuario"
              [disabled]="cargando"
              required
            />
          </div>

          <div class="form-group">
            <label for="contrasena">Contraseña:</label>
            <input 
              type="password" 
              id="contrasena" 
              [(ngModel)]="contrasena" 
              name="contrasena"
              [disabled]="cargando"
              required
            />
          </div>

          <div *ngIf="error" class="error-message">
            {{ error }}
          </div>

          <button 
            type="submit" 
            [disabled]="cargando"
            class="btn-login"
          >
            {{ cargando ? 'Cargando...' : 'Iniciar Sesión' }}
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    }

    .login-box {
      background: white;
      padding: 40px;
      border-radius: 8px;
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
      width: 100%;
      max-width: 400px;
    }

    .login-box h2 {
      text-align: center;
      color: #333;
      margin-bottom: 30px;
      font-size: 24px;
    }

    .login-form {
      margin-bottom: 20px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    .form-group label {
      display: block;
      margin-bottom: 5px;
      color: #555;
      font-weight: 500;
    }

    .form-group input {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 14px;
      box-sizing: border-box;
      transition: border-color 0.3s;
    }

    .form-group input:focus {
      outline: none;
      border-color: #667eea;
      box-shadow: 0 0 5px rgba(102, 126, 234, 0.3);
    }

    .form-group input:disabled {
      background-color: #f5f5f5;
      cursor: not-allowed;
    }

    .error-message {
      background-color: #fee;
      color: #c33;
      padding: 10px;
      border-radius: 4px;
      margin-bottom: 20px;
      border-left: 4px solid #c33;
      font-size: 14px;
    }

    .btn-login {
      width: 100%;
      padding: 12px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 4px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: opacity 0.3s;
    }

    .btn-login:hover:not(:disabled) {
      opacity: 0.9;
    }

    .btn-login:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  `]
})
export class LoginComponent implements OnInit {
  usuario = '';
  contrasena = '';
  error = '';
  cargando = false;
  returnUrl: string = '/tickets';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Si ya está autenticado, redirigir a tickets
    if (this.authService.estaAutenticado()) {
      this.router.navigate(['/tickets']);
      return;
    }

    // Capturar la URL de retorno
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/tickets';
  }

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
        this.router.navigate([this.returnUrl]);
        this.cargando = false;
      },
      (error) => {
        this.error = error.error?.message || 'Usuario o contraseña incorrectos';
        this.cargando = false;
      }
    );
  }
}

