import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, map, throwError } from 'rxjs';
import { Tipo } from './tipo';

@Injectable({
  providedIn: 'root'
})
export class TipoService {

  private urlEndPoint:string='http://localhost:8080/api/tipos';

  constructor(private httpClient:HttpClient,private router:Router) { 
  }
  getAll():Observable<Tipo[]>{
    return this.httpClient.get<Tipo[]>(this.urlEndPoint).pipe(
       map(response =>{
        return response.map(x=>{
          
          return x;
        });
      })
      );
  }
  getTipo(id: number):Observable<Tipo>{
    return this.httpClient.get<Tipo>(`${this.urlEndPoint}/${id}`);
  }




  create(tipo:Tipo):Observable<any>{
    return this.httpClient.post<any>(this.urlEndPoint,tipo).pipe(
      catchError(e=>{
        if(e.status=400){
          return throwError(()=>e);
        }
                return throwError(()=>e);
    }));
  }


  update(tipo:Tipo):Observable<any>{
    return this.httpClient.put<any>(this.urlEndPoint.concat('/'+tipo.id),tipo).pipe(
      catchError(e=>{
        if(e.status=400){
          return throwError(()=>e);
        }
        console.log(e.error.mensaje);
        return throwError(()=>e);
      })
    );
  }

  deleteTipo(id:number):Observable<Tipo>{
    return this.httpClient.delete<Tipo>(`${this.urlEndPoint}/${id}`);
  }
  
}
