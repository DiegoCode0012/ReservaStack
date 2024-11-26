
export class User{
    id:number;
    nombre:string;
    username:string;
    email:string;
    password:string;
    roles:Roles[];
    constructor() {}
}

export class Roles{
    id:number;
    name:string;
}