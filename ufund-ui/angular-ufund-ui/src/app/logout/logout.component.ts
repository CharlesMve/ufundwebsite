import { Component, OnInit } from '@angular/core';
import { LoginService } from '../shared/login.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {
  constructor(private loginService: LoginService, private router: Router) { }

  logout(): void {
    this.loginService.setIsAdmin(false);
    this.loginService.setIsLoggedIn(false);
    this.router.navigate(['login']);
  }

  ngOnInit(): void {
      this.loginService.setUsername("");
      this.logout();
  }
}
