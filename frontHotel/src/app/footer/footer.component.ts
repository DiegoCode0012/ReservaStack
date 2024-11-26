import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  isUser=false;
  isAdmin = false;

  constructor(private tokenService: TokenService) { }

  ngOnInit() {
   this.isUser=this.tokenService.isUser();
   this.isAdmin=this.tokenService.isAdmin();
  }

}
