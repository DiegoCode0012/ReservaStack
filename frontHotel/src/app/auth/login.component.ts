import { Component } from '@angular/core';
import { LoginUsuario } from '../models/login-usuario';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginUsuario: LoginUsuario = new LoginUsuario();
  errMsj: string;
  incorrecto:boolean=false;
  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private router: Router,
  ) { }

  ngOnInit() {
    const state = history.state;
    this.incorrecto = state.incorrecto;
  }


  onLogin(): void {
    this.authService.login(this.loginUsuario).subscribe(
      data => {
        this.tokenService.setToken(data.token);
        console.log( this.tokenService.getToken());
        if(this.tokenService.isAdmin() || this.tokenService.isUser()){
          window.location.href="/users";
        }else if(this.tokenService.isClient()){
          window.location.href="/inicio";
        }
      },
      err => {
        this.errMsj = err.error.errorMessage;
       console.log(err.error.errorMessage);
      }
    );
  }

}
