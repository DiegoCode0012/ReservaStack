import { Room } from "../rooms/room";
import { User } from "../users/user";
export class Reserva{
    id:number;
    diaStart:Date;
    diaEnd:Date;
    habitacion:Room;
    estado:string;
    user:User;
    idPago:string;
}