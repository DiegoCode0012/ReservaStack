import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateChildFn, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';

//PROTEGER LAS RUTAS URL
 export const authGuard: CanActivateFn = (route:ActivatedRouteSnapshot, state:RouterStateSnapshot) => {
  const token=inject(TokenService);
  const router = inject(Router);
  if(token.isLogged()){ // si esta logueado y si el token tiene rol admin, me permite     
    return true;
  }else{ 
    router.navigate(['/login']);
    return false;
  }
};



