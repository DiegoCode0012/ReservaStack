import { Component, Input } from '@angular/core';
import { Reserva } from '../reservas/reserva';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-finalizado',
  templateUrl: './finalizado.component.html',
  styleUrls: ['./finalizado.component.css']
})
export class FinalizadoComponent {
  
  reserva: Reserva = null;
  isClient:boolean=false;
  logeado:boolean=false;
  constructor(private tk:TokenService,private router: Router) {
    this.isClient=tk.isClient();
    this.logeado=tk.isLogged();
  }

  ngOnInit(): void {
    this.reserva = history.state.reserva;
    if(this.isClient && this.logeado){
        
          if(this.reserva == null || this.reserva == undefined){
            this.router.navigate(['/inicio']);
          }else{
            Swal.fire({
              title: "Reserva creada",
              text: "Reserva creada con exito :" + this.reserva.idPago,
              icon: "success"
            }); 
          }
  }else{
    this.router.navigate(['/login']);
  }
}
}
