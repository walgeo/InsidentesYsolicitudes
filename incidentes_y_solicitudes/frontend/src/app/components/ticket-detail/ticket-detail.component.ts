import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../services/ticket.service';
import { Ticket, Comentario } from '../../models/ticket.model';

@Component({
  selector: 'app-ticket-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './ticket-detail.component.html',
  styleUrls: ['./ticket-detail.component.css']
})
export class TicketDetailComponent implements OnInit {
  ticket?: Ticket;
  comentarios: Comentario[] = [];
  nuevoComentario: string = '';
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ticketService: TicketService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTicket(id);
    this.loadComentarios(id);
  }

  loadTicket(id: number): void {
    this.loading = true;
    this.ticketService.getTicket(id).subscribe({
      next: (data) => {
        this.ticket = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading ticket', err);
        this.loading = false;
      }
    });
  }

  loadComentarios(ticketId: number): void {
    this.ticketService.getComentariosByTicket(ticketId).subscribe({
      next: (data) => this.comentarios = data,
      error: (err) => console.error('Error loading comentarios', err)
    });
  }

  addComentario(): void {
    if (!this.nuevoComentario.trim() || !this.ticket?.id) return;

    const comentario: Comentario = {
      ticketId: this.ticket.id,
      mensaje: this.nuevoComentario,
      creadoPor: 'Usuario' // En producción vendría del usuario autenticado
    };

    this.ticketService.createComentario(comentario).subscribe({
      next: () => {
        this.loadComentarios(this.ticket!.id!);
        this.nuevoComentario = '';
      },
      error: (err) => console.error('Error adding comentario', err)
    });
  }

  deleteTicket(): void {
    if (!this.ticket?.id) return;

    if (confirm('¿Estás seguro de eliminar este ticket?')) {
      this.ticketService.deleteTicket(this.ticket.id).subscribe({
        next: () => this.router.navigate(['/tickets']),
        error: (err) => console.error('Error deleting ticket', err)
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado?.toUpperCase()) {
      case 'ABIERTO': return 'badge-info';
      case 'EN_PROGRESO': return 'badge-warning';
      case 'RESUELTO': return 'badge-success';
      case 'CERRADO': return 'badge-danger';
      default: return 'badge-info';
    }
  }
}

