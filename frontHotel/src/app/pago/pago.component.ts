import { Component, EventEmitter, Output } from '@angular/core';
import { Reserva } from '../reservas/reserva';
import { TokenService } from '../service/token.service';
import { Router } from '@angular/router';
// import { soloNumeros } from './pago';
import { ScriptLoaderService } from './pago';
import { ReservaService } from '../reservas/reserva.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { User} from '../users/user';

@Component({
  selector: 'app-pago',
  templateUrl: './pago.component.html',
  styleUrls: ['./pago.component.css']
})
export class PagoComponent {

reserva:Reserva=null;
isClient:boolean=false;
logeado:boolean=false;
incorrecto:boolean=false;
msjError:string[];
pagoExitoso:boolean=false;
codigo:string=null;
formularioPago: FormGroup;
errores: string[] = [];
token:string;
user:User=null;
clientId:number=null;
validar:boolean=false;
  constructor(private fb: FormBuilder,private tk:TokenService,private router: Router,
    private scriptLoader: ScriptLoaderService,private reservaservice:ReservaService,
    private authService:AuthService) { 
    this.isClient=tk.isClient();
    this.logeado=tk.isLogged();
    this.token=tk.getToken();
    this.formularioPago = this.fb.group({
      tarjeta: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      nombre: ['', Validators.required],
      mes: ['', Validators.required],
      año: ['', Validators.required],
      cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3}$')]], 
    });
  }
  @Output() reservaEmitida: EventEmitter<Reserva> = new EventEmitter();
    ngOnInit(): void {
      
      const storedReserva = localStorage.getItem('reserva');
      if (storedReserva) {
        
        this.reserva = JSON.parse(storedReserva);
        console.log(this.reserva);
          if(this.validarErrores().length >0){
            this.router.navigate(['/inicio'], {
              state: { msjError: this.msjError.join("|") },
            });
          }
      }
      if (!this.isClient || !this.logeado) {
        this.router.navigate(['/login'], {
          state: { incorrecto: true },
        });
      }
      if(this.token!=null){
          this.clientId=this.tk.getClientId();
        this.authService.getUsuario(this.clientId).subscribe(objetoUser => {
          this.reserva.user = objetoUser;
      });
      }
      }
      validarErrores(){
        this.msjError=[];
          if(!this.reserva.diaStart || this.reserva.diaStart ==undefined){
            this.msjError.push("Seleccione la fecha de inicio");
          }
          if (!this.reserva.diaEnd || this.reserva.diaEnd ==undefined){
            this.msjError.push("Seleccione la fecha final");
          } 
          if (!this.reserva.habitacion || this.reserva.habitacion==undefined || this.reserva.habitacion==null){
            this.msjError.push("Seleccione una habitación");
          }
          if(this.reserva.diaStart>this.reserva.diaEnd){
            this.msjError.push("La fecha de inicio no puede ser mayor o igual a la fecha final");
          }
          return this.msjError;
      }



  ProcesarPago() {
    this.errores = []; // Limpiamos errores anteriores
    console.log(this.reserva);
    if (this.formularioPago.invalid) {
    
      Object.keys(this.formularioPago.controls).forEach((campo) => {
        const control = this.formularioPago.get(campo);
        if (control?.errors) {
          if (control.errors['required']) {
            this.errores.push(`El campo ${campo} es obligatorio.`);
          }
          if (control.errors['pattern']) {
            this.errores.push(`El campo ${campo} tiene un formato inválido.`);
          }
        }
      });
      return;
    }


        this.codigo=this.getUid();
        this.reserva.idPago=this.codigo;
        this.createReserva();

  }

      

      createReserva(){
        console.log(this.reserva);
        this.reserva.estado='Activa';
         this.reservaservice.create(this.reserva).subscribe(json=>{
     
          
 this.router.navigate(['/finalizado'], {
  state: { reserva: this.reserva },
});
         },err=>{
          this.errores=err.error.errors;
          console.log(err);
         }
         )
       }
       
     

       getUid(): string {
        const unique = crypto.randomUUID();
        return unique.substring(27); 
      }
}

