import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ErrorMessageService {

  /**
   * Extrae un mensaje de error clara y útil del error HTTP
   */
  getErrorMessage(error: any): string {
    // Si hay un mensaje personalizado del backend
    if (error?.error?.message) {
      return error.error.message;
    }

    // Si es un error de validación
    if (error?.error?.details) {
      const fieldErrors = Object.entries(error.error.details)
        .map(([field, msg]) => `${field}: ${msg}`)
        .join(', ');
      return `Errores de validación: ${fieldErrors}`;
    }

    // Si hay un mensaje de razón
    if (error?.error?.reason) {
      return error.error.reason;
    }

    // Errores HTTP específicos
    switch (error?.status) {
      case 400:
        return 'Error de validación. Por favor revisa los datos ingresados.';
      case 404:
        return 'El recurso no fue encontrado.';
      case 409:
        return error?.error?.message || 'Conflicto al procesar la solicitud.';
      case 500:
        return 'Error interno del servidor. Por favor intenta de nuevo.';
      default:
        return error?.error?.message || 'Ocurrió un error desconocido.';
    }
  }

  /**
   * Determina si el error es de transición de estado
   */
  isStateTransitionError(error: any): boolean {
    const message = error?.error?.message || '';
    return message.includes('Transición') ||
           message.includes('estado') ||
           message.includes('ABIERTO') ||
           message.includes('EN_PROGRESO') ||
           message.includes('RESUELTO') ||
           message.includes('CERRADO');
  }

  /**
   * Determina si el error es de campos obligatorios
   */
  isValidationError(error: any): boolean {
    const message = error?.error?.message || '';
    return message.includes('requerido') ||
           message.includes('Campo') ||
           message.includes('no puede estar vacío');
  }
}

