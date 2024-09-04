import { Component, OnInit } from '@angular/core';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { LoginService } from '../shared/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  needs: Need[] = [];

  constructor(private needService: NeedService, private loginService: LoginService, private router: Router) { }

  sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  async ngOnInit(): Promise<void> { 
    await this.sleep(150);
    if (this.loginService.getIsLoggedIn() == false){
      this.router.navigate(['login']);
      return;
    }
    this.getNeeds();
  }

  getNeeds(): void {
    this.needService.getNeeds()
      .subscribe(needs => this.needs = needs.slice(1, 5));
  }
}