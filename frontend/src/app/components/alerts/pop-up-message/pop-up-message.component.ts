import { Component, EventEmitter, Input, Output } from '@angular/core';
import AlertProps from '../AlertProps';
import { AlertService } from '../alert-service.service';

@Component({
  selector: 'app-pop-up-message',
  templateUrl: './pop-up-message.component.html',
  styleUrls: ['./pop-up-message.component.css']
})
export class PopUpMessageComponent {
  constructor(private alertsService: AlertService) { }
  @Output() confirmed = new EventEmitter<boolean>();
  alertProps: AlertProps = new AlertProps();

  close(b: boolean) {
    this.alertsService.hide();
  }

  confirm() {
    if (this.alertProps.onConfirm) {
      this.alertProps.onConfirm();
    }
    this.alertsService.hide();
  }

  ngAfterViewInit(): void {
    this.alertsService.alertprops.subscribe((alertProps) => {
      this.alertProps = alertProps;
    });
  }

  // Helper methods for message styling and content
  isSuccessMessage(): boolean {
    return this.alertProps.message.includes('🎉') ||
           this.alertProps.message.includes('🎣') ||
           this.alertProps.message.includes('Welcome') ||
           this.alertProps.message.includes('successful');
  }

  isErrorMessage(): boolean {
    return this.alertProps.message.includes('❌') ||
           this.alertProps.message.includes('failed') ||
           this.alertProps.message.includes('error') ||
           this.alertProps.message.includes('Invalid');
  }

  isInfoMessage(): boolean {
    return !this.isSuccessMessage() && !this.isErrorMessage();
  }

  getMessageTitle(): string {
    if (this.isSuccessMessage()) {
      return 'Success!';
    } else if (this.isErrorMessage()) {
      return 'Oops!';
    } else {
      return 'Information';
    }
  }

  getMessageBody(): string {
    // Remove emoji prefixes for cleaner display
    return this.alertProps.message
      .replace(/^(🎉|🎣|❌|👋|ℹ️)\s*/, '')
      .trim();
  }

  getButtonClasses(): string {
    if (this.isSuccessMessage()) {
      return 'bg-green-500 hover:bg-green-600 text-white focus:ring-green-300';
    } else if (this.isErrorMessage()) {
      return 'bg-red-500 hover:bg-red-600 text-white focus:ring-red-300';
    } else {
      return 'bg-blue-500 hover:bg-blue-600 text-white focus:ring-blue-300';
    }
  }
}
