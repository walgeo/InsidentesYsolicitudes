import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

interface LoginResponse {
  token: string;
  usuario: string;
  nombre: string;
  apellido: string;
  rol: string;
  usuarioId: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';
  private usuarioActual = new BehaviorSubject<any>(null);
  public usuarioActual$ = this.usuarioActual.asObservable();

  constructor(private http: HttpClient) {
    this.cargarUsuarioDelStorage();
  }

  login(usuario: string, contrasena: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { usuario, contrasena })
      .pipe(
        tap((response) => {
          this.guardarToken(response.token);
          this.usuarioActual.next(response);
        }),
        catchError((error) => {
          return throwError(() => error);
        })
      );
  }

  register(usuarioData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, usuarioData);
  }

  guardarToken(token: string): void {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  cerrarSesion(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    this.usuarioActual.next(null);
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }

  obtenerUsuarioActual(): any {
    return this.usuarioActual.value;
  }

  validarToken(token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/validar-token`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  private cargarUsuarioDelStorage(): void {
    const usuario = localStorage.getItem('usuario');
    if (usuario) {
      this.usuarioActual.next(JSON.parse(usuario));
    }
  }
}

