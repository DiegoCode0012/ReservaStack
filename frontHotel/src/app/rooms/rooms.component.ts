import { Component } from '@angular/core';
import { RoomService } from './room.service';
import { Room } from './room';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { TipoService } from '../tipos/tipo.service';
import { Tipo } from '../tipos/tipo';
import { TokenService } from '../service/token.service';
import { HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
})
export class RoomsComponent {
listTipos: Tipo[];
ListRooms:Room[];
archivos: File[] = [];
public errores :string[];
public errorFoto: string;
p: number = 1;
room:Room=new Room();
isAdmin=false;
fotoSeleccionada: File;
progreso: number = 0;
  constructor(private roomservice:RoomService,
    private tipoService:TipoService,
    private activatedRoute:ActivatedRoute, 
    private tokenService: TokenService
    ,private router:Router){
    this.getAll();
    this.listarTypes();
    this.isAdmin = this.tokenService.isAdmin();

  }
  
  getAll(){
    this.roomservice.getAll().subscribe(x=>{
      return this.ListRooms=x;
    })
  }

  cargarRoom(id:number){

    this.activatedRoute.params.subscribe(params =>{
        this.roomservice.getRoom(id).subscribe(ObjetoRoom =>this.room =ObjetoRoom)   
  }
    )
  }

  seleccionarFoto(event) {
    const tiposPermitidos = ['image/png'];
    this.fotoSeleccionada=event.target.files[0];
    console.log(this.archivos);
    
    if(this.fotoSeleccionada.type.indexOf('image') < 0)
      {
        Swal.fire('Error seleccionar imagen:', 'El archivo debe ser de tipo imagen', 'error');
        this.fotoSeleccionada = null;
      }else if(tiposPermitidos.includes(this.fotoSeleccionada.type)){
        this.archivos.push(this.fotoSeleccionada);
      }else {
        Swal.fire('Error al seleccionar imagen:', 'El archivo debe ser de tipo PNG', 'error');

      }
  }

  subirFoto() {

    if(this.archivos.length === 0) {
      Swal.fire('Error upload:', 'Debe seleccionar una foto', 'error');
    }
    else {
      this.roomservice.subirFoto(this.archivos, this.room.id)
        .subscribe(event => {
      if(event.type === HttpEventType.Response)
          {
            let response: any = event.body;
            this.room = response.room as Room;

           // this.modalService.notificarUpload.emit(this.room);

            Swal.fire(
              'La foto se ha subido correctamente',
              response.mensaje,
              'success');
          }
          this.archivos =[];
        }, err=>{
          console.log(err);
            Swal.fire('Error:', err.error.mensaje);
          
        });
    }
  }

  public create():void{
    this.room.disponible=true;
    this.roomservice.create(this.room).subscribe(json =>{
  this.getAll();
  this.resetForm();
  Swal.fire({
    title: "Nueva habitación",
    text: "Habitación creada con exito",
    icon: "success"
  });
      
      },
      err=>{
        this.errores=err.error.errors;
      }
    )
  }
  public resetForm():void {
    this.room= new Room();
    this.errores=[];
  }
  updateRoom():void{
    this.roomservice.update(this.room).subscribe(json => {
      this.getAll();
      this.resetForm();
      Swal.fire({
        title: "Habitación Editada",
        text: "Habitación editada con exito",
        icon: "success"
      });
    }, err=>{
      this.errores=err.error.errors
      console.error(err);
    }
    )
  }
  
  listarTypes(){
    this.tipoService.getAll().subscribe(x=>{
      return this.listTipos=x;
    })
  }
  checkTipo(h1: Room, h2: Room): boolean {
    return h1 != undefined && h2 != undefined && h1.id == h2.id;
  }
  delete(objeto:Room){
    Swal.fire({
      title: 'Estas seguro?',
      text: `¿Seguro que desea eliminar la habitacion con id ${objeto.id}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si eliminar'
    }).then(x=>{
      if (x.isConfirmed) {
        this.roomservice.deleteRoom(objeto.id).subscribe(rpta =>{
          this.getAll();
          Swal.fire({
            title: "Habitación Eliminada",
            text: "Habitación Eliminada con exito",
            icon: "success"
          });
  
        })
        
      }
  
    })
  }

}
