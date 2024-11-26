import { Component, OnDestroy, OnInit } from '@angular/core';
import { RoomService } from '../rooms/room.service';
import { Room } from '../rooms/room';
import { Reserva } from '../reservas/reserva';
import { TipoService } from '../tipos/tipo.service';
import { Tipo } from '../tipos/tipo';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
habitaciones:Room[];
listTipo:Tipo[];
habitacionSeleccionado: Room | undefined;
minDateEnd: Date;
maxDateEnd: Date;
minDateStart: Date;
maxDateStart: Date;
reserva:Reserva=new Reserva();
type_id:number | null;
p: number = 1;
msjError:string[];

constructor(private romS: RoomService, private tipoS:TipoService, private  activatedRoute: ActivatedRoute) {}
ngOnInit(): void {
  this.listarHabitacionesDisponibles();
  this.listarTipos();

  const state = history.state;
  if (state.msjError && typeof state.msjError === 'string' && state.msjError.length > 0) {
    console.log("msjError recibido: " + state.msjError);
    this.msjError = state.msjError.split("|");
  }
  const currentTime = new Date();

    this.minDateStart= new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate());
    this.maxDateStart= new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+7);

    this.minDateEnd = new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+1); //empieza desde el año recurrente , en enero, y en el tercer dia de ese mes
    this.maxDateEnd = new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+8);

    const storedReserva = localStorage.getItem('reserva');
    if (storedReserva) {
      this.reserva = JSON.parse(storedReserva);
    }

  console.log(this.reserva);
}


listarHabitacionesDisponibles(){
  localStorage.setItem('reserva', JSON.stringify(this.reserva));
   this.romS.getAvailableRooms().subscribe(x=>{
  return this.habitaciones=x;
 })
}

abrirModal(habitacion: any) {
  this.habitacionSeleccionado = habitacion;
  const modal = new (window as any).bootstrap.Modal(document.getElementById('habitacionModal'));
  modal.show();
}
listarTipos(){
  this.tipoS.getAll().subscribe(x=>{
    return this.listTipo=x;
  });
}

cambiarLocalStorageDiaStart(){
  const ahora = new Date();
  const fechaSeleccionada = new Date(this.reserva.diaStart);

  fechaSeleccionada.setHours(ahora.getHours());
  fechaSeleccionada.setMinutes(ahora.getMinutes());

  this.reserva.diaStart = fechaSeleccionada;

  // Guarda el objeto actualizado en el localStorage
  localStorage.setItem('reserva', JSON.stringify(this.reserva));

  // Muestra `diaStart` y `diaEnd` en consola para verificar
  console.log('diaStart:', this.reserva.diaEnd);
}

cambiarLocalStorageDiaEnd(){
  const ahora = new Date();
  const fechaSeleccionada = new Date(this.reserva.diaEnd);

  // Asigna la hora actual al objeto `fechaSeleccionada`
  fechaSeleccionada.setHours(8, 0, 0, 0);

  // Actualiza `diaEnd` con la nueva fecha
  this.reserva.diaEnd = fechaSeleccionada;

  // Guarda el objeto actualizado en el localStorage
  localStorage.setItem('reserva', JSON.stringify(this.reserva));

  // Muestra `diaStart` y `diaEnd` en consola para verificar
  console.log('diaEnd:', this.reserva.diaEnd);
}

filtrarHabitacionesXTipo(){
  if(this.type_id ==undefined){
     this.listarHabitacionesDisponibles();
  }else{
    this.romS.RoomsByTypeId(this.type_id).subscribe(x=>{
        this.habitaciones=x;
    })
  }
}
setear(){
  this.reserva.habitacion=this.habitacionSeleccionado;
  localStorage.setItem('reserva',JSON.stringify(this.reserva));
  window.location.href="/pago";
}

}
