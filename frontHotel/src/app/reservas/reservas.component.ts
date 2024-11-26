import { Component } from '@angular/core';
import { ReservaService } from './reserva.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Reserva } from './reserva';
import { RoomService } from '../rooms/room.service';
import Swal from 'sweetalert2';
import * as pdfMake from "pdfmake/build/pdfmake";
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { TipoService } from '../tipos/tipo.service';
import { Tipo } from '../tipos/tipo';
import { Room } from '../rooms/room';
import { TokenService } from '../service/token.service';
import { CalendarOptions,EventInput  } from '@fullcalendar/core'; // useful for typechecking
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import timeGridPlugin from '@fullcalendar/daygrid';
import { AuthService } from '../service/auth.service';
import { User } from '../users/user';
import { HttpClient } from '@angular/common/http';

(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;
@Component({
  selector: 'app-reservas',
  templateUrl: './reservas.component.html',
})
export class ReservasComponent {
  public errores :string[];
 type_id:number;
listReserva:Reserva[] =[];
listTipo:Tipo[];
listRooms:Room[];
listClientesSinReserva:User[];
  reserva:Reserva=new Reserva();
 reservaSeleccionada: Reserva; 
  minDateEnd: Date;
maxDateEnd: Date;
minDateStart: Date;
maxDateStart: Date;

calendarOptions: CalendarOptions = {
  initialView: 'dayGridMonth',
  plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin],
  events: [],
};

handleEventClick(eventInfo) {
  console.log(eventInfo);
  this.reservaSeleccionada = eventInfo.event.extendedProps.reserva;  // Puedes acceder al ID de la reserva
  const descripcion = eventInfo.event.title;  // Accede a la descripción del evento
  const modalElement = document.getElementById('exampleModalDetail');
  const modal = new (window as any).bootstrap.Modal(modalElement);
  modal.show();
}


isAdmin=false;
isUser=false;
  constructor(private reservaservice:ReservaService, 
    private roomservice:RoomService,
    private tipoService:TipoService,
    private authservice:AuthService,
    private router:Router,private activatedRoute:ActivatedRoute,
    private tokenService:TokenService, private http:HttpClient){
      this.isAdmin = this.tokenService.isAdmin();
      this.isUser=this.tokenService.isUser();

  }
  imagePath: string = 'assets/angular.jpg';

  ngOnInit(): void {
    const currentTime = new Date();

    this.minDateStart= new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate());
    this.maxDateStart= new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+7);

    this.minDateEnd = new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+1); //empieza desde el año recurrente , en enero, y en el tercer dia de ese mes
    this.maxDateEnd = new Date(currentTime.getFullYear(), currentTime.getMonth(),currentTime.getDate()+8);
    this.listarReservas();
    this.listarTipos();
    this.listarClientes();
  }
  listarTipos(){
    this.tipoService.getAll().subscribe(x=>{
      this.listTipo=x;
      return this.listReserva;
    });
  }
  filtrarHabitacionesXTipo(){
    this.roomservice.RoomsByTypeId(this.type_id).subscribe(x=>{
        this.listRooms=x;
        console.log(this.listRooms);
        return this.listRooms;
    })
  }

  listarClientes(){
    this.authservice.getAllSinReserva().subscribe(x=>{
      this.listClientesSinReserva=x;
      return this.listClientesSinReserva;
    });
  }
  listarReservas(){
    this.reservaservice.getAll().subscribe(x=>{
      this.listReserva=x;
      this.updateCalendarEvents(x);
      return this.listReserva;
    });
  }
 
  updateCalendarEvents(x:Reserva[]) {
    this.calendarOptions = { 
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin],
      events: x.map(reserva => ({
        title: `Reserva ${reserva.user.nombre}`,
        start: reserva.diaStart,
        end: reserva.diaEnd,
        description: `Reserva con ID ${reserva.id}`,
        textColor: "white",
        backgroundColor: reserva.estado === 'CONCLUIDO' ? 'green' : 'blue', // Cambia el color de fondo según el estado
        extendedProps: {
          reserva :reserva
        }
      })),
      eventClick:this.handleEventClick.bind(this)
    };
  }

  
createReserva(){
  console.log(this.reserva);
  this.reserva.estado='Activa';
   this.reservaservice.create(this.reserva).subscribe(json=>{
    this.listarReservas();
    this.listarClientes();
    this.resetForm();
    Swal.fire({
      title: "Reserva creada",
      text: "Reserva creada con exito",
      icon: "success"
    });   
   },err=>{
    this.errores=err.error.errors;
    console.log(err);
   }
   )
 }
  public resetForm():void {
    this.reserva= new Reserva();
    this.errores=[];
    this.type_id=undefined;
    this.listRooms=[];
  }
  delete(objeto:Reserva){
    console.log(objeto.diaStart);
    Swal.fire({
      title: 'Estas seguro?',
      text: `¿Seguro que desea eliminar la reserva con id ${objeto.id}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si eliminar'
    }).then(x=>{
      if (x.isConfirmed) {
        this.reservaservice.delete(objeto.id).subscribe(rpta =>{
          this.listarReservas();
          Swal.fire({
            title: "Reserva eliminada",
            text: "Reserva eliminada con exito",
            icon: "success"
          });  
  
        })
        
      }
  
    })

  }

  async convertLocalImageToBase64(imagePath: string): Promise<string> {
    try {
        console.log(imagePath);
      if(imagePath=='http://localhost:8080/api/uploads/img/'){
        imagePath='assets/imagenes/imagenSinFoto.png';
      }
        const response = await this.http.get(imagePath, { responseType: 'blob' }).toPromise();
        const blob = response;
        
        return await new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onloadend = () => resolve(reader.result as string);
            reader.onerror = reject;
            reader.readAsDataURL(blob); // Convertir Blob a Base64
        });
    } catch (error) {
        // Imagen por defecto en caso de error
        return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/wcAAwAB/CBaEoYAAAAASUVORK5CYII=';
    }
}

  async dowloand(reserva: Reserva) {
    try {
      // Convertimos las imágenes de la habitación a Base64
      const  angular   =   'assets/imagenes/VistaHotel2.png';
      const imageAngular = await this.convertLocalImageToBase64(angular);
      const base64Images = await Promise.all([
        this.convertLocalImageToBase64(`http://localhost:8080/api/uploads/img/${reserva.habitacion.foto1}`),
        this.convertLocalImageToBase64(`http://localhost:8080/api/uploads/img/${reserva.habitacion.foto2}`),
        this.convertLocalImageToBase64(`http://localhost:8080/api/uploads/img/${reserva.habitacion.foto3}`)
      ]);
  
      const x = reserva.diaEnd;
      let EndDay = new Date(x);
      const m = reserva.diaStart;
      let StartDay = new Date(m);
      let diasTotales = EndDay.getDate() - StartDay.getDate();
  
      const pdfdefinition: any = {
        background:[
           {
            image:imageAngular,
            width: 595,
            height: 842,
            opacity:0.3
          }
        ],
        content: [
        {
          image:imageAngular,
          width:500,
          height: 250,
          margin: [0, 0, 0, 20]
        },
          {
            text: [
              { text: 'DATOS DEL CLIENTE/HUESPED: \n', style: 'header' },
              '===================================================\n',
              { text: 'Nombre:', style: 'subtitle' },
              reserva.user.nombre,
              { text: '\n Pais:', style: 'subtitle' },
              'Peru',
              { text: '\n USERNAME:', style: 'subtitle' },
              reserva.user.username,
              { text: '\n EMAIL:', style: 'subtitle' },
              reserva.user.email, '\n\n',
              { text: 'DATOS DE LA AGENCIA/EMPRESA: \n', style: 'header' },
              '===================================================\n',
              { text: 'Empresa:', style: 'subtitle' },
              'San Diego Peru  \n \n',
              { text: 'DATOS DE LA RESERVA \n', style: 'header' },
              '===================================================\n',
              { text: 'Estado de la Reserva:', style: 'subtitle' },
              reserva.estado + '\n\n',
              { text: 'Confirmacion:', style: 'subtitle' },
              reserva.id,
              { text: '\n Fecha Entrada:', style: 'subtitle' },
              reserva.diaStart,
              { text: '\n Fecha Salida:', style: 'subtitle' },
              reserva.diaEnd,
              { text: '\n Habitaciones:', style: 'header' },
              ' 1 Noches:' + diasTotales + '\n\n\n',
              { text: 'Detalle de las habitaciones reservadas:', style: 'header', alignment: 'center' },
            ],
          },
          {
            style: 'tableExample',
            table: {
              headerRows: 1,
              body: [
                [
                  { text: 'ID', style: 'subtitle' },
                  { text: 'N:Habitacion', style: 'subtitle' },
                  { text: 'Tipo', style: 'subtitle' },
                  { text: 'Precio', style: 'subtitle' },
                ],
                [
                  '' + reserva.id,
                  '' + reserva.habitacion.numero,
                  '' + reserva.habitacion.tipo.descripcion,
                  'S/' + reserva.habitacion.tipo.precio,
                ]
              ]
            },
            layout: 'headerLineOnly'
          },
          {
            // Agregar imágenes en una nueva fila en la tabla de manera individual
            style: 'tableExample2',
            table: {
              body: [
                base64Images.map(image => ({
                  image: image,
                  width: 150,
                  height: 100,
                  margin: [20, 0, 10, 10] // Espacio entre imágenes
                }))
              ]
            },
            layout: 'noBorders'
          }
        ],
      
        styles: {
          header: {
            fontSize: 13,
            bold: true,
          },
          izquierda: {
            alignment: 'right',
          },
          subtitle: {
            fontSize: 12,
            bold: true,
          },
          tableExample: {
            margin: [170, 15, 0, 0], // LEFT, TOP, RIGHT, BOTTOM
            alignment: 'center'
          },
          tableExample2: {
            margin: [0, 15, 0, 0], // LEFT, TOP, RIGHT, BOTTOM
            alignment: 'center'
          }
        }
      };
      
      const pdf = pdfMake.createPdf(pdfdefinition);
      pdf.open();
    } catch (error) {
      console.error('Error al cargar las imágenes:', error);
    }
  }

  

}
