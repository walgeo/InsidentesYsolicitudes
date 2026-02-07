import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../services/ticket.service';
import { Ticket, Estado, Prioridad } from '../../models/ticket.model';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {
  tickets: Ticket[] = [];
  estados: Estado[] = [];
  prioridades: Prioridad[] = [];

  // Filtros
  filterAplicacion: string = '';
  filterEstadoId?: number;
  filterPrioridadId?: number;
  filterFechaDesde: string = '';
  filterFechaHasta: string = '';

  // Ordenamiento
  sortField: string = 'creadoEn';
  sortDirection: string = 'desc';

  // Paginación
  currentPage: number = 0;
  pageSize: number = 10;
  totalElements: number = 0;
  totalPages: number = 0;

  loading: boolean = false;

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.loadEstados();
    this.loadPrioridades();
    this.loadTickets();
  }

  loadEstados(): void {
    this.ticketService.getEstados().subscribe({
      next: (data) => this.estados = data,
      error: (err) => console.error('Error loading estados', err)
    });
  }

  loadPrioridades(): void {
    this.ticketService.getPrioridades().subscribe({
      next: (data) => this.prioridades = data,
      error: (err) => console.error('Error loading prioridades', err)
    });
  }

  loadTickets(): void {
    this.loading = true;
    console.log('🔍 Cargando tickets...');
    console.log('Parámetros:', {
      page: this.currentPage,
      size: this.pageSize,
      aplicacion: this.filterAplicacion,
      estadoId: this.filterEstadoId,
      prioridadId: this.filterPrioridadId,
      fechaDesde: this.filterFechaDesde,
      fechaHasta: this.filterFechaHasta,
      sort: `${this.sortField},${this.sortDirection}`
    });

    // Solo enviar sort si es diferente del default
    const sort = (this.sortField === 'creadoEn' && this.sortDirection === 'desc')
      ? undefined
      : [`${this.sortField},${this.sortDirection}`];

    this.ticketService.getTickets(
      this.currentPage,
      this.pageSize,
      this.filterAplicacion || undefined,
      this.filterEstadoId,
      this.filterPrioridadId,
      this.filterFechaDesde || undefined,
      this.filterFechaHasta || undefined,
      sort
    ).subscribe({
      next: (response) => {
        console.log('✅ Respuesta recibida:', response);
        console.log('📋 Tickets recibidos:', response.content);
        console.log('📊 Total elementos:', response.totalElements);
        this.tickets = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        console.error('❌ Error loading tickets:', err);
        console.error('Error status:', err.status);
        console.error('Error message:', err.message);
        console.error('Error completo:', JSON.stringify(err, null, 2));
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadTickets();
  }

  clearFilters(): void {
    this.filterAplicacion = '';
    this.filterEstadoId = undefined;
    this.filterPrioridadId = undefined;
    this.filterFechaDesde = '';
    this.filterFechaHasta = '';
    this.currentPage = 0;
    this.loadTickets();
  }

  sortBy(field: string): void {
    if (this.sortField === field) {
      // Toggle direction
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.loadTickets();
  }

  getSortIcon(field: string): string {
    if (this.sortField !== field) return '↕️';
    return this.sortDirection === 'asc' ? '↑' : '↓';
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadTickets();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadTickets();
    }
  }

  deleteTicket(id: number): void {
    if (confirm('¿Estás seguro de eliminar este ticket?')) {
      this.ticketService.deleteTicket(id).subscribe({
        next: () => this.loadTickets(),
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

