import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, map, throwError } from 'rxjs';
import { Reserva } from './reserva';
import { DatePipe } from '@angular/common';
import {HabitacionesMasSolicitadas } from '../models/HabitacionesMasSolicitadas';
import { GananciaTipoDTO } from '../models/GananciaTipoDTO';
import { GananciaPorMesDTO } from '../models/GananciaPorMesDTO';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private urlEndPoint:string='http://localhost:8080/api/reservas';

  constructor(private httpClient:HttpClient) { }

getAll ():Observable<Reserva[]>{
  return this.httpClient.get<Reserva[]>(this.urlEndPoint).pipe(
    map(response => {
      let x=response;
      return x.map(l=>{
        let datepipe=new DatePipe('es');
        return l;
      });
    })
  );
}




HabitacionesMasSolicitadas():Observable<HabitacionesMasSolicitadas[]>{
  return this.httpClient.get<HabitacionesMasSolicitadas[]>(this.urlEndPoint.concat('/habitacionesMasSolicitadas')).pipe(
    map(response => {
      let x=response;
      return x.map(l=>{
        let datepipe=new DatePipe('es');
        return l;
      });
    })
  );
}


GananciasPorTipo():Observable<GananciaTipoDTO[]>{
  return this.httpClient.get<GananciaTipoDTO[]>(this.urlEndPoint.concat('/ganaciasxTipo')).pipe(
    map(response => {
      let x=response;
      return x.map(l=>{
        return l;
      });
    })
  );
}

totalClientes():Observable<Number>{
  return this.httpClient.get<Number>(this.urlEndPoint.concat('/totalClientes')).pipe(
    map(response => {
      return response;
    })
  );
} 

GananciasTotales():Observable<Number>{
  return this.httpClient.get<Number>(this.urlEndPoint.concat('/obtenerGananciasTotales')).pipe(
    map(response => {
      return response;
    })
  );
} 

GananciasPorMes():Observable<GananciaPorMesDTO[]>{
  return this.httpClient.get<GananciaPorMesDTO[]>(this.urlEndPoint.concat('/obtenerGananciasMensuales')).pipe(
    map(response => {
      return response;
    })
  );
}



  getReserva(id: number):Observable<Reserva>{
    return this.httpClient.get<Reserva>(this.urlEndPoint+id);
  }

  getReservaByUser(id: number):Observable<Reserva []>{
    return this.httpClient.get<Reserva[]>(`${this.urlEndPoint.concat('xUsuario')}/${id}`).pipe(
      map(response => {
        let x=response;
        return x.map(l=>{
          let datepipe=new DatePipe('es');
          return l;
        });
      })
    );
    //return this.httpClient.get<Reserva>(`$this.urlEndPoint.concat('xUsuario')/${id}`);
  }

  create(reserva:Reserva):Observable<any>{
    return this.httpClient.post<any>(this.urlEndPoint,reserva).pipe(
      catchError(e=>{
        if(e.status=400){
          return throwError(()=>e);
        }
                return throwError(()=>e);
    })
    );
  }
  delete(id:number):Observable<any>{
    return this.httpClient.delete<any>(`${this.urlEndPoint}/${id}`);
  }
 
}
