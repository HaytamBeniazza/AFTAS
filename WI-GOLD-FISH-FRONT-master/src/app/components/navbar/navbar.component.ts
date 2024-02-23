import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { TUser } from 'src/app/model/TUser';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  user : Observable<TUser>;
  constructor(private authService : AuthService){
    this.user = authService.authenticatedUser;
  }
  logout(){
    this.authService.logout();
  }
}
