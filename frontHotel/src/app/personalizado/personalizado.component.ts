import { Component, OnInit } from '@angular/core';
import { ReservaService } from '../reservas/reserva.service';
import { Reserva } from '../reservas/reserva';
import * as pdfMake from "pdfmake/build/pdfmake";
import { TokenService } from '../service/token.service';
import { HttpClient } from '@angular/common/http';
import * as XLSX from 'xlsx';
@Component({
  selector: 'app-personalizado',
  templateUrl: './personalizado.component.html',
  styleUrls: ['./personalizado.component.css']
})
export class PersonalizadoComponent implements OnInit{
  listReserva:Reserva[] =[];
  reserva:Reserva;
  p: number = 1;

  constructor(private reservaservice:ReservaService, private tk:TokenService, private http:HttpClient){
  }
  ngOnInit(): void {
    this.getReservaByUser();
  }
  fileName='ExcelSheet.xlsx';
  exportExcel(){
    let data = document.getElementById("table-data");
  const ws: XLSX.WorkSheet=XLSX.utils.table_to_sheet(data);
  const wb: XLSX.WorkBook=XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb,ws,'Sheet1');
  XLSX.writeFile(wb,this.fileName);
}
  getReservaByUser(){
    const id=this.tk.getClientId();
    console.log(id);
    this.reservaservice.getReservaByUser(id).subscribe(x=>{
      this.listReserva=x;
      return this.listReserva;
    });
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
