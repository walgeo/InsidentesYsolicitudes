import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../services/ticket.service';
import { ErrorMessageService } from '../../services/error-message.service';
import { ErrorAlertComponent } from '../error-alert/error-alert.component';
import { Ticket, Estado, Prioridad } from '../../models/ticket.model';

@Component({
  selector: 'app-ticket-form',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, ErrorAlertComponent],
  templateUrl: './ticket-form.component.html',
  styleUrls: ['./ticket-form.component.css']
})
export class TicketFormComponent implements OnInit {
  ticket: Ticket = {
    titulo: '',
    descripcion: '',
    tipo: 'INCIDENTE',
    aplicacion: '',
    prioridadId: 1,
    estadoId: 1,
    creadoPor: 'Usuario'
  };

  estados: Estado[] = [];
  prioridades: Prioridad[] = [];
  isEditMode: boolean = false;
  loading: boolean = false;

  // Error handling
  errorMessage: string = '';
  errorDetails: string[] = [];
  showError: boolean = false;
  isStateTransitionError: boolean = false;

  // Validation errors
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private ticketService: TicketService,
    private errorService: ErrorMessageService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEstados();
    this.loadPrioridades();

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadTicket(Number(id));
    }
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

  loadTicket(id: number): void {
    this.ticketService.getTicket(id).subscribe({
      next: (data) => this.ticket = data,
      error: (err) => console.error('Error loading ticket', err)
    });
  }

  onSubmit(): void {
    this.fieldErrors = {};
    this.errorMessage = '';
    this.errorDetails = [];
    this.showError = false;

    // Validar campos obligatorios
    if (!this.ticket.titulo || this.ticket.titulo.trim() === '') {
      this.fieldErrors['titulo'] = 'El título es obligatorio.';
    }
    if (!this.ticket.aplicacion || this.ticket.aplicacion.trim() === '') {
      this.fieldErrors['aplicacion'] = 'La aplicación es obligatoria.';
    }
    if (!this.ticket.tipo || this.ticket.tipo.trim() === '') {
      this.fieldErrors['tipo'] = 'El tipo es obligatorio (Incidente o Solicitud).';
    }
    if (!this.ticket.prioridadId) {
      this.fieldErrors['prioridadId'] = 'La prioridad es obligatoria.';
    }
    if (!this.ticket.estadoId) {
      this.fieldErrors['estadoId'] = 'El estado es obligatorio.';
    }

    // Si hay errores de validación, mostrarlos
    if (Object.keys(this.fieldErrors).length > 0) {
      this.isStateTransitionError = false;
      this.errorMessage = 'Por favor completa los siguientes campos:';
      this.errorDetails = Object.entries(this.fieldErrors).map(
        ([field, error]) => `${this.getFieldLabel(field)}: ${error}`
      );
      this.showError = true;
      return;
    }

    this.loading = true;

    const operation = this.isEditMode
      ? this.ticketService.updateTicket(this.ticket.id!, this.ticket)
      : this.ticketService.createTicket(this.ticket);

    operation.subscribe({
      next: () => {
        this.router.navigate(['/tickets']);
      },
      error: (err) => {
        console.error('Error saving ticket:', err);

        const message = this.errorService.getErrorMessage(err);
        this.isStateTransitionError = this.errorService.isStateTransitionError(err);

        this.errorMessage = message;

        // Si es un error de validación con detalles
        if (err?.error?.details) {
          this.errorDetails = Object.entries(err.error.details)
            .map(([field, msg]: [string, any]) => `${this.getFieldLabel(field)}: ${msg}`);
        }

        this.showError = true;
        this.loading = false;
      }
    });
  }

  private getFieldLabel(field: string): string {
    const labels: { [key: string]: string } = {
      titulo: 'Título',
      descripcion: 'Descripción',
      tipo: 'Tipo',
      aplicacion: 'Aplicación',
      prioridadId: 'Prioridad',
      estadoId: 'Estado',
      creadoPor: 'Creado por',
      asignadoA: 'Asignado a'
    };
    return labels[field] || field;
  }

  cancel(): void {
    this.router.navigate(['/tickets']);
  }
}

