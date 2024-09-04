import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  public isLoggedIn: boolean = false;
  public isAdmin: boolean = false;
  public username: String = "";

  constructor() { }

  setIsLoggedIn(value: boolean): void {
    this.isLoggedIn = value;
  }
  getIsLoggedIn(): boolean {
    return this.isLoggedIn;
  }

  setIsAdmin(value: boolean): void {
    this.isAdmin = value;
  }
  getIsAdmin(): boolean {
    return this.isAdmin;
  }

  setUsername(value: String): void {
    this.username = value;
  }
  getUsername(): String {
    return this.username;
  }
}
