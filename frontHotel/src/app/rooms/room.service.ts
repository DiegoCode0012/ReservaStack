import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Room } from './room';
import {catchError, map} from 'rxjs/operators';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class RoomService {

  private urlEndPoint:string='http://localhost:8080/api/habitaciones';
  
  constructor(private httpClient:HttpClient,private router:Router) { 
  }

  
  getAll():Observable<Room[]>{
    return this.httpClient.get<Room[]>(this.urlEndPoint).pipe(
       map(x=>{ //lista
        return x.map(m=>{ 
        //  m.tipo.descripcion=m.tipo.descripcion.toUpperCase();
          return m;
        });
       })
      );
  }

  getAvailableRooms():Observable<Room[]>{
    return this.httpClient.get<Room[]>(this.urlEndPoint.concat('disponibles')).pipe(
       map(response =>{
        return response.map(x=>{
          return x;
        });
      })
      );
  }


  getRoom(id: number):Observable<Room>{
    return this.httpClient.get<Room>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        return throwError(() => e);
    })
    )
  }




  create(room:Room):Observable<any>{
    return this.httpClient.post<any>(this.urlEndPoint,room).pipe(
      catchError(e=>{

        if(e.status=400){
          return throwError(()=>e);
        }
        return throwError(()=>e);
      })
    );
  }


  update(room:Room):Observable<any>{
    return this.httpClient.put<any>(`${this.urlEndPoint}/${room.id}`,room).pipe(
      catchError(e=>{
        if(e.status=400){
          return throwError(()=>e);
        }
        console.log(e.error.mensaje);
        return throwError(()=>e);
      })
    );
  }
  deleteRoom(id:number):Observable<Room>{
    return this.httpClient.delete<Room>(`${this.urlEndPoint}/${id}`);
  }
 
  RoomsByTypeId(id:number):Observable<Room[]>{
    return this.httpClient.get<Room[]>(this.urlEndPoint.concat("/idTipo/"+id));
  };

  subirFoto(archivos: File[], id): Observable<HttpEvent<{}>> {
    let formData = new FormData();
    archivos.forEach(archivo => {
      formData.append('archivos', archivo); // 'archivos' es el nombre del parámetro
    });
    formData.append("id", id);

    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, {
      reportProgress: true
    });
    return this.httpClient.request(req).pipe(
      catchError(e =>{
    return throwError(()=>e);}
  ));
  }


}