import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ticket, PagedResponse, Estado, Prioridad, Comentario } from '../models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = '/api/tickets';
  private estadosUrl = '/api/estados';
  private prioridadesUrl = '/api/prioridades';
  private comentariosUrl = '/api/comentarios';

  constructor(private http: HttpClient) {}

  // Tickets
  getTickets(
    page: number = 0,
    size: number = 10,
    aplicacion?: string,
    estadoId?: number,
    prioridadId?: number,
    creadoDesde?: string,
    creadoHasta?: string,
    sort?: string[]
  ): Observable<PagedResponse<Ticket>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (aplicacion) params = params.set('aplicacion', aplicacion);
    if (estadoId) params = params.set('estadoId', estadoId.toString());
    if (prioridadId) params = params.set('prioridadId', prioridadId.toString());
    if (creadoDesde) params = params.set('creadoDesde', creadoDesde);
    if (creadoHasta) params = params.set('creadoHasta', creadoHasta);
    if (sort && sort.length > 0) {
      sort.forEach(s => params = params.append('sort', s));
    }

    const url = `${this.apiUrl}?${params.toString()}`;
    console.log('🌐 Realizando petición GET a:', url);

    return this.http.get<PagedResponse<Ticket>>(this.apiUrl, { params });
  }

  getTicket(id: number): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.apiUrl}/${id}`);
  }

  createTicket(ticket: Ticket): Observable<Ticket> {
    return this.http.post<Ticket>(this.apiUrl, ticket);
  }

  updateTicket(id: number, ticket: Ticket): Observable<Ticket> {
    return this.http.put<Ticket>(`${this.apiUrl}/${id}`, ticket);
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Estados
  getEstados(): Observable<Estado[]> {
    return this.http.get<Estado[]>(this.estadosUrl);
  }

  // Prioridades
  getPrioridades(): Observable<Prioridad[]> {
    return this.http.get<Prioridad[]>(this.prioridadesUrl);
  }

  // Comentarios
  getComentariosByTicket(ticketId: number): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.comentariosUrl}/ticket/${ticketId}`);
  }

  createComentario(comentario: Comentario): Observable<Comentario> {
    return this.http.post<Comentario>(this.comentariosUrl, comentario);
  }
}

