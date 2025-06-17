import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TUser } from '../../model/TUser';

@Component({
  selector: 'app-pending-approval',
  templateUrl: './pending-approval.component.html',
  styleUrls: ['./pending-approval.component.css']
})
export class PendingApprovalComponent implements OnInit {
  user: TUser = {} as TUser;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.authenticatedUser.subscribe(user => {
      this.user = user;
    });
  }

  isPendingApproval(): boolean {
    return this.user && this.user.role === 'NONE';
  }

  logout(): void {
    this.authService.logout();
  }
}
