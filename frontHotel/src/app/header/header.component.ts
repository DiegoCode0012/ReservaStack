import { Component, OnInit } from "@angular/core";
import { TokenService } from "../service/token.service";
import { RoomService } from "../rooms/room.service";
import { TipoService } from "../tipos/tipo.service";
import { Room } from "../rooms/room";
import { Tipo } from "../tipos/tipo";
import { Reserva } from "../reservas/reserva";
@Component({
    selector:'app-header',
    templateUrl:`./header.component.html`,
    styleUrls: ['./header.component.css']

})
//aqui se definen las palabras claves que se puede usar en el app.component.html
export class HeaderComponent implements OnInit{
    isLogged=false;
    username:string;
    isAdmin=false;
    isUser=false;
    isClient=false;  
    constructor(private tokenService: TokenService,private romS: RoomService, private tipoS:TipoService) { }
    ngOnInit() {
     this.isLogged = this.tokenService.isLogged();
     this.username=this.tokenService.getUserName();
     this.isAdmin=this.tokenService.isAdmin();
     this.isClient=this.tokenService.isClient();
     this.isUser=this.tokenService.isUser();
    }
  
    onLogOut(): void {
      this.tokenService.logOut();
    }
   

}