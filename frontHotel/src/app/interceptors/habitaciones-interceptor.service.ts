import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from '../service/token.service';

@Injectable({
  providedIn: 'root'
})
// el interceptor permite configurar la cabecera de authorizacion de POSTMAN Y asi colocar el token ahi
//permite modificar las peticiones cuando se ejecutan
export class HabitacionInterceptorService    implements HttpInterceptor{

  constructor(private tokenService:TokenService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let intReq=req;
    const token=this.tokenService.getToken();

    if(token != null){
      intReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${ token }`
        }
        });
    }
    return next.handle(intReq);
  }
  
}
export const interceptorProvider = [{ provide: HTTP_INTERCEPTORS, useClass: HabitacionInterceptorService, multi: true }];

