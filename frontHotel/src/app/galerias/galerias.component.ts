import { Component } from '@angular/core';
import { Room } from '../rooms/room';
import { RoomService } from '../rooms/room.service';

@Component({
  selector: 'app-galerias',
  templateUrl: './galerias.component.html',
  styleUrls: ['./galerias.component.css']
})
export class GaleriasComponent {

  habitaciones:Room[];
habitacionSeleccionado: Room | undefined;
constructor(private romS: RoomService) {}

  ngOnInit(): void {
    this.listarHabitacionesDisponibles();
    console.log(this.habitaciones);
  }
  listarHabitacionesDisponibles(){
     this.romS.getAvailableRooms().subscribe(x=>{
    return this.habitaciones=x;
   })
  }
  
}
