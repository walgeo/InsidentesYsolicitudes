import { Component, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
    <nav class="navbar" *ngIf="estaAutenticado">
      <div class="container">
        <h1 class="logo">📋 Incidentes y Solicitudes</h1>
        <div class="nav-links">
          <a routerLink="/tickets" routerLinkActive="active">Tickets</a>
          <a routerLink="/tickets/new" class="btn btn-primary">+ Nuevo Ticket</a>
          <div class="user-info" *ngIf="usuarioActual">
            <span class="username">👤 {{ usuarioActual.nombre }} {{ usuarioActual.apellido }}</span>
            <span class="role">({{ usuarioActual.rol }})</span>
          </div>
          <button (click)="logout()" class="btn btn-logout">🚪 Cerrar Sesión</button>
        </div>
      </div>
    </nav>
    <main class="container">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .navbar {
      background-color: #2c3e50;
      color: white;
      padding: 15px 0;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      margin-bottom: 30px;
    }

    .navbar .container {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .logo {
      font-size: 24px;
      font-weight: 600;
    }

    .nav-links {
      display: flex;
      gap: 20px;
      align-items: center;
    }

    .nav-links a {
      color: white;
      text-decoration: none;
      padding: 8px 15px;
      border-radius: 4px;
      transition: background-color 0.3s;
    }

    .nav-links a:hover {
      background-color: #34495e;
    }

    .nav-links a.active {
      background-color: #3498db;
    }

    .user-info {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      padding: 0 10px;
      border-left: 1px solid rgba(255,255,255,0.2);
      margin-left: 10px;
    }

    .username {
      font-size: 14px;
      font-weight: 500;
    }

    .role {
      font-size: 12px;
      opacity: 0.8;
      text-transform: uppercase;
    }

    .btn-logout {
      background-color: #e74c3c;
      color: white;
      border: none;
      padding: 8px 15px;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      font-weight: 500;
      transition: background-color 0.3s;
    }

    .btn-logout:hover {
      background-color: #c0392b;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 20px;
    }
  `]
})
export class AppComponent implements OnInit {
  title = 'Incidentes y Solicitudes';
  estaAutenticado = false;
  usuarioActual: any = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Suscribirse a cambios en el estado de autenticación
    this.authService.usuarioActual$.subscribe((usuario) => {
      this.usuarioActual = usuario;
      this.estaAutenticado = !!usuario;
    });

    // Verificar si ya hay un usuario autenticado
    this.estaAutenticado = this.authService.estaAutenticado();
    this.usuarioActual = this.authService.obtenerUsuarioActual();
  }

  logout() {
    if (confirm('¿Estás seguro que deseas cerrar sesión?')) {
      this.authService.cerrarSesion();
      this.router.navigate(['/login']);
    }
  }
}

