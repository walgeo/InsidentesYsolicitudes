import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-error-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="show" class="alert alert-danger alert-dismissible fade show" role="alert">
      <div class="alert-header">
        <strong>❌ Error</strong>
        <button type="button" class="btn-close" (click)="close()"></button>
      </div>
      <div class="alert-body">
        <p>{{ message }}</p>
        <hr *ngIf="details && details.length > 0">
        <ul *ngIf="details && details.length > 0" class="mb-0">
          <li *ngFor="let detail of details">{{ detail }}</li>
        </ul>
      </div>
    </div>
  `,
  styles: [`
    .alert {
      margin-bottom: 20px;
      border-left: 4px solid #dc3545;
    }

    .alert-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      padding-bottom: 10px;
      border-bottom: 1px solid #f5c6cb;
    }

    .alert-body {
      color: #721c24;
    }

    .alert-body p {
      margin-bottom: 0;
      line-height: 1.6;
    }

    .alert-body ul {
      margin: 10px 0 0 20px;
      padding: 0;
    }

    .alert-body li {
      margin-bottom: 5px;
    }

    .btn-close {
      background: none;
      border: none;
      cursor: pointer;
      font-size: 20px;
      color: #721c24;
    }

    .btn-close:hover {
      opacity: 0.7;
    }
  `]
})
export class ErrorAlertComponent {
  @Input() show = false;
  @Input() message = '';
  @Input() details: string[] = [];

  @Output() onClose = new EventEmitter<void>();

  close() {
    this.show = false;
    this.onClose.emit();
  }
}

