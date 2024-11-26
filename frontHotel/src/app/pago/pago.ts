import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root', // Esto registra el servicio como un proveedor global.
})
export class ScriptLoaderService {
    private loadedScripts: Set<string> = new Set();

    loadScript(scriptUrl: string): Promise<void> {
      return new Promise((resolve, reject) => {
        if (this.loadedScripts.has(scriptUrl)) {
          resolve(); // El script ya está cargado
          return;
        }
  
        const script = document.createElement('script');
        script.src = scriptUrl;
        script.async = true;
        script.onload = () => {
          this.loadedScripts.add(scriptUrl);
          resolve();
        };
        script.onerror = (err) => reject(err);
        document.body.appendChild(script);
      });
    }
}

