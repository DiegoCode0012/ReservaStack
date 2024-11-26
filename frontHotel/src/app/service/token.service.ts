import { Injectable } from '@angular/core';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'token';

  constructor(
    private router: Router
  ) { }

  public setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  public isLogged(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }

    try {
      const exp = this.getTokenExpiration(token);
      const now = Date.now();
      if (now > exp) {
        console.log("Token expirado");
        this.clearSession();
        return false;
      }
      return true;
    } catch (error) {
      console.error("Error al verificar el token:", error);
      this.clearSession();
      return false;
    }
  }

  public getUserName(): string | null {
    const payload = this.getTokenPayload();
    return payload ? payload.sub : null;
  }

  public isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  public isUser(): boolean {
    return this.hasRole('ROLE_USER');
  }

  public isClient(): boolean {
    return this.hasRole('ROLE_CLIENTE');
  }

  public getClientId(): number | null {
    const payload = this.getTokenPayload();
    return payload ? payload.userId : null;
  }

  public logOut(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  // Métodos auxiliares

  private getTokenExpiration(token: string): number {
    const payload = this.decodeTokenPayload(token);
    return payload.exp * 1000;
  }

  private hasRole(role: string): boolean {
    const payload = this.getTokenPayload();
    return payload?.roles?.includes(role) || false;
  }

  private getTokenPayload(): any | null {
    const token = this.getToken();
    if (!token) {
      return null;
    }
    try {
      return this.decodeTokenPayload(token);
    } catch (error) {
      console.error("Error al decodificar el token:", error);
      this.clearSession();
      return null;
    }
  }

  private decodeTokenPayload(token: string): any {
    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) {
      throw new Error("Token inválido");
    }
    return JSON.parse(atob(payloadBase64));
  }

  private clearSession(): void {
    localStorage.clear();
  }
}
