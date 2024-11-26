import { Component } from '@angular/core';
import { User } from './user';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import swal from 'sweetalert2';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import Swal from 'sweetalert2';
import { TokenService } from '../service/token.service';
@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
})

export class UsersComponent {
  public errores :string[];
user:User=new User();
isAdmin=false;
p: number = 1;
  //formReactive:FormGroup= new FormGroup({});
 //modalDetalle:boolean=false;
listUser:User []=[];
isUpdate: boolean =false;
  constructor(private tokenService:TokenService,private uService:AuthService,private activatedRoute:ActivatedRoute, private router:Router){
    this.isAdmin = this.tokenService.isAdmin();
  }
ngOnInit(): void{
this.listar();

}
listar(){
  this.uService.listarUsuarios().subscribe(x=>{
    this.listUser=x;
    return this.listUser;
  });
}
getRoles(roles: any[]): string {
  return roles.map(role => role.name).join(', ');
}

public resetForm():void {
  this.user= new User();
  this.errores=[];
}

delete(objeto:User){
  Swal.fire({
    title: 'Estas seguro?',
    text: `¿Seguro que desea eliminar el User ${objeto.nombre} con id ${objeto.id}?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Si eliminar'
  }).then(x=>{
    if (x.isConfirmed) {
      this.uService.deleteUser(objeto.id).subscribe(rpta =>{
        this.listar();
        Swal.fire(
          'Usuario eliminado ',
          'success'
        )

      })
      
    }

  })
}
 /*
 abrir(item: any){
  this.isUpdate = true;
  this.formReactive.controls['id'].setValue(item.id);
  this.formReactive.controls['name'].setValue(item.name);
  this.formReactive.controls['email'].setValue(item.email);

}*/

/*update(){
  this.uService.update(this.formReactive.value).subscribe(x => {
    swal.fire('Usuario Actualizado', `${x.name}:  ${x.email}`, 'success');
    this.listar();
  })
} */



}
