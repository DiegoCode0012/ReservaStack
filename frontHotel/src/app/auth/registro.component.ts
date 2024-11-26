import { Component } from '@angular/core';
import { User } from '../users/user';
import { AuthService } from '../service/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
})
export class RegistroComponent {

  public errores :string[];
  user:User=new User();
  constructor(private authservice:AuthService){

  };
  
  public resetForm():void {
    this.user= new User();
    this.errores=[];
  }

  public create():void{
    this.authservice.nuevo(this.user).subscribe(json =>{
  this.resetForm();
        Swal.fire('Nuevo Usuario creado con exito', 'success');      
      },
      err=>{
        this.errores=err.error.errors;
      }
    )
  }
}
