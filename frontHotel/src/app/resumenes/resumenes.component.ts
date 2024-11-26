import { Component, OnInit } from '@angular/core';
import { ReservaService } from '../reservas/reserva.service';
import { HabitacionesMasSolicitadas } from '../models/HabitacionesMasSolicitadas';
import { GananciaTipoDTO } from '../models/GananciaTipoDTO';
import { Chart, ChartData, ChartOptions } from 'chart.js';

@Component({
  selector: 'app-resumenes',
  templateUrl: './resumenes.component.html',
  styleUrls: ['./resumenes.component.css']
})
export class ResumenesComponent implements OnInit{
  listHM:HabitacionesMasSolicitadas[] =[];
  listGananciasPorTipo:GananciaTipoDTO[]=[];
  totalClients:Number;
  totalReservas:Number;
  gananciaTotal:Number;
  chartDataNumeroHabitacion: number[] = [];
  chartLabelsNumeroHabitacion: string[] = [];
  public chartData: ChartData<'bar'>={
    labels:[],
    datasets:[]
  };

 

  public chartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { display: false },
      title: { display: true, text: 'Ganancias Mensuales' }
    }
  };
  constructor(private rS:ReservaService){
}
ngOnInit(): void {
  this.HabitacionesMas();
  this.GananciasPorTipo();
  this.TotalClients();
  this.ReservasTotales();
  this.GananciasTotales();
  this.GananciasMensuales();
}

HabitacionesMas(){
  this.rS.HabitacionesMasSolicitadas().subscribe(x=>{
    this.listHM=x;
  });
}

GananciasPorTipo(){
  this.rS.GananciasPorTipo().subscribe(x=>{
    this.listGananciasPorTipo=x;
  });
}

TotalClients(){
  this.rS.totalClientes().subscribe(x=>{
    this.totalClients=x;
  });
}

ReservasTotales(){
  this.rS.getAll().subscribe(x=>{
    this.totalReservas=x.length;
  });
}

GananciasTotales(){
  this.rS.GananciasTotales().subscribe(x=>{
    this.gananciaTotal=x;
  });
}
GananciasMensuales(){
  this.rS.GananciasPorMes().subscribe(data=>{
    const meses = data.map(d => `Mes ${d.mes}`);
    const ganancias = data.map(d => d.totalGanancias);
    this.chartData = {
      labels: meses,
      datasets: [
        { label: 'Ganancias', data: ganancias, backgroundColor: '#28a745',barThickness:120 }
      ]
    };
  });
}


}
