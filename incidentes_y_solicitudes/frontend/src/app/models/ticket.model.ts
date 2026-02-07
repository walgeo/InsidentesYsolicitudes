export interface Ticket {
  id?: number;
  titulo: string;
  descripcion?: string;
  tipo: string;
  aplicacion: string;
  prioridadId: number;
  prioridadNombre?: string;
  estadoId: number;
  estadoNombre?: string;
  asignadoA?: string;
  creadoPor?: string;
  creadoEn?: string;
  actualizadoPor?: string;
  actualizadoEn?: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Estado {
  id: number;
  nombre: string;
}

export interface Prioridad {
  id: number;
  nombre: string;
}

export interface Comentario {
  id?: number;
  ticketId: number;
  mensaje: string;
  creadoPor?: string;
  creadoEn?: string;
}

