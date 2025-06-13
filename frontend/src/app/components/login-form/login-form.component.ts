import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {
  visible: boolean = true;

  ngOnInit() {
      
  }

  showDialog() {
    this.visible = true;
  }
  loginForm: FormGroup;
  constructor(private fb: FormBuilder, private AuthService: AuthService) {
    this.loginForm = this.fb.group({
      username: [''],
      password: [''],
    });
  }
  onSubmit() {
    this.AuthService.login(this.loginForm.value.username, this.loginForm.value.password);
  }
}
