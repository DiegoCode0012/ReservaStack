import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { LoginUsuario } from '../models/login-usuario';
import { JwtDTO } from '../models/jwt-DTO';
import { User } from '../users/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authURL="http://localhost:8080/auth/";

  constructor(private httpClient: HttpClient) { }

  public nuevo(user: User): Observable<any> {
    return this.httpClient.post<any>(this.authURL.concat('nuevo'),user).pipe(
      catchError(e=>{

        if(e.status===400){
          return throwError(()=>e);
        }
        return throwError(()=>e);
      })
    );
  }

  public login(loginUsuario: LoginUsuario): Observable<JwtDTO> {
    return this.httpClient.post<JwtDTO>(this.authURL + 'login', loginUsuario).pipe(
      catchError(e=>{

        if(e.status=400){
          return throwError(()=>e);
        }
        return throwError(()=>e);
      })
    );
  }

  public listarUsuarios(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.authURL.concat('users'));
  }
  public getUsuario(id:number): Observable<User> {
    return this.httpClient.get<User>(`${this.authURL.concat('users')}/${id}`);
  }

  getAllSinReserva():Observable<User[]>{
    return this.httpClient.get<User[]>(this.authURL.concat("clientesSinReserva")).pipe(
      );
  }
  
  deleteUser(id:number):Observable<User>{
    return this.httpClient.delete<User>(`${this.authURL.concat('users')}/${id}`);
  }
}
