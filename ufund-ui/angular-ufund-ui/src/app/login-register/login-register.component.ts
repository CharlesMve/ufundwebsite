import { Component, NgZone } from '@angular/core'; // Import NgZone
import { LoginService } from '../shared/login.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent {
    username: string;
    username2: string;
    private apiUrl = 'http://localhost:8080/users'; 

    httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(
        private http: HttpClient, 
        private loginService: LoginService, 
        private router: Router,
        private ngZone: NgZone 
    ) { 
        this.username = '';
        this.username2 = '';
    }

    checkUsernameExists(username: string): Observable<any> {
        return this.http.get(`${this.apiUrl}/${username}`);
    }
    checkUsernameExistsBool(username: string): Observable<any> {
        return this.http.get(`${this.apiUrl}/check/${username}`);
    }

    createUser(user: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, user, this.httpOptions);
    }

    onSubmit() {
        this.checkUsernameExists(this.username).subscribe(
          response => {
              this.loginService.setIsAdmin(response.username === 'admin');
              this.loginService.setIsLoggedIn(true);
              this.loginService.setUsername(response.username);
          },
          error => {
              if (error.status === 404) {
                console.log('Username does not exist');
              } else {
                console.error('An error occurred:', error);
              }
              return;
          }
        );
        this.router.navigate(['dashboard']);
    }

    sleep(ms: number): Promise<void> {
      return new Promise(resolve => setTimeout(resolve, ms));
    }

    async onSubmitRegister() {
      this.checkUsernameExistsBool(this.username2).subscribe(
        async response => {
            if (response) {
              console.log('Choose different username');
            }
            else {
              console.log('Creating username');
              const user = {
                username: this.username2,
                basket: {}
              }
              this.createUser(user).subscribe(
                response => {
                  console.log("Successful user creation");
                  this.loginService.setUsername(this.username2);
                  this.loginService.setIsLoggedIn(true);
                  this.router.navigate(['dashboard']);
                }
              )
              
            }
            return;
        }
      );
  }
}
