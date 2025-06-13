import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { TUser } from 'src/app/model/TUser';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {
  user: Observable<TUser>;

  constructor(private authService: AuthService) {
    this.user = authService.authenticatedUser;
  }
}
