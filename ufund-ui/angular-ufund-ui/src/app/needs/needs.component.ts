import { Component, OnInit } from '@angular/core';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { LoginService } from '../shared/login.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, max, tap } from 'rxjs';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
})
export class NeedsComponent implements OnInit {
  fundingBasket: Array<Need> = [];
  needs: Need[] = [];
  showNeedsList: boolean = false;
  amount: number = 1;
  currentUserMoney: number =  0;

  private usersUrl = 'http://localhost:8080/users';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  constructor(private http: HttpClient, private needService: NeedService, public loginService: LoginService, private router: Router) { }

  sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  async ngOnInit(): Promise<void> { 
    // Sleep for API
    await this.sleep(150);
    // If not logged in, reroute to login page
    if (this.loginService.getIsLoggedIn() == false){
      this.router.navigate(['login']);
      return;
    }
    // Check if user is admin
    this.showNeedsList = this.loginService.getIsAdmin();
    // if admin, get all needs
    if (this.loginService.getIsAdmin()) {
      this.getNeeds();
    }
    // else get needs that are not in funding basket
    else {
      this.getNeedsHelper();
      const currentUser = await this.needService.getUser(this.loginService.getUsername()).toPromise();
      this.currentUserMoney = currentUser.money;
      console.log(currentUser);
    }
  }

  getNeeds(): void {
    // get needs through api and assign it to local needs
    this.needService.getNeeds()
    .subscribe(needs => this.needs = needs);
  }

  getNeedsHelper(): void {
    // get the funding basket with username through API
    this.needService.getUser(this.loginService.username)
    .subscribe(user => {
      this.fundingBasket = user.basket.needs;
      let temp: Need[] = [];
      // get needs through API
      this.needService.getNeeds()
      .subscribe(needs => {
        needs.forEach(need => {
          let bool = false;
          this.fundingBasket.forEach(need2 => {
            console.log(`Basket need id: ${need2.id} - need id: ${need.id}`);
            if (need.id === need2.id) {
              bool = true;
            }
          })
          if (bool == false) {
            temp.push(need);
          }
        })
        this.needs = temp;
      });
    });
  }
  

  add(name: string, costStr: string, quantityStr: string): void {
    name = name.trim();
    var cost: number = +costStr;
    var quantity: number = +quantityStr;
    if (!name) { return; }
    this.needService.addNeed({ name, cost, quantity } as Need)
      .subscribe(need => {
        this.needs = [...this.needs, need];
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(h => h !== need);
    this.needService.deleteNeed(need.id).subscribe();
  }

  addNeed(need: any, quantity: number, maxQuantity: number){
    if (quantity > maxQuantity) return;
    let newNeed = need;
    newNeed.quantity = quantity;
    this.fundingBasket.push(newNeed);
    const user = {
      username: this.loginService.getUsername(),
      basket: {
        "needs": this.fundingBasket
      }
    }
    this.updateUser(user).subscribe(response => {
      console.log(response);
    });
    this.getNeedsHelper();
    this.router.navigate(['/needs']);
  }

  shareNeed(need: any) {
    this.needService.shareNeed(need).subscribe(
      () => {
        this.router.navigate(['/needs']);
      }
    )
  }

  removeNeed(need: any) {
    const index = this.fundingBasket.indexOf(need, 0);
    if (index > -1) {
      this.fundingBasket.splice(index, 1);
    }
    const user = {
      username: this.loginService.getUsername(),
      basket: {
        "needs": this.fundingBasket
      }
    }
    this.updateUser(user).subscribe(response => {
      console.log(response);
    });
    this.getNeedsHelper();
    this.router.navigate(['/needs']);
  }

  async checkOut(need: Need): Promise<void> {
    const totalCost = need.quantity * need.cost;  // Assuming 'cost' field exists in 'Need'
    const currentUser = await this.needService.getUser(this.loginService.getUsername()).toPromise();

    if (currentUser.money >= totalCost) {
      currentUser.money -= totalCost; 
      await this.updateUser(currentUser).toPromise();
      if (currentUser.money >= totalCost) {
        currentUser.money -= totalCost; 
        await this.updateUser(currentUser).toPromise();
      
        // Update the component's currentUserMoney to reflect the new balance
        this.currentUserMoney = currentUser.money;
      } else {
        console.error('Not enough money for this transaction');
        return;
      }
      
    } else {
      console.error('Not enough money for this');
      return;
    }
    try {
      let originalNeed = await this.needService.getNeed(need.id).toPromise();
      
      if (need.quantity === originalNeed!.quantity) {
        await this.needService.deleteNeed(need.id).toPromise();
      } else {
        originalNeed!.quantity -= need.quantity;
        await this.needService.updateNeed(originalNeed!).toPromise();
        this.updateFundingBasketQuantity(need);
      }
      this.removeFromFundingBasket(need);
      await this.updateUserFundingBasket();
      this.getNeedsHelper();
      this.router.navigate(['/needs']);
    } catch (error) {
      console.error('Error in checkout process:', error);
    }
  }
  
  private removeFromFundingBasket(need: Need): void {
    this.fundingBasket = this.fundingBasket.filter(basketNeed => basketNeed.id !== need.id);
  }
  
  private updateFundingBasketQuantity(need: Need): void {
    const basketNeed = this.fundingBasket.find(basketNeed => basketNeed.id === need.id);
    if (basketNeed) {
      basketNeed.quantity -= need.quantity;
    }
  }
  
  private async updateUserFundingBasket(): Promise<void> {
    const user = {
      username: this.loginService.getUsername(),
      basket: { needs: this.fundingBasket }
    };
    await this.updateUser(user).toPromise();
  }
  

  updateUser(user: any): Observable<any>  {
    return this.http.put<any>(`${this.usersUrl}/${this.loginService.getUsername()}`, user, this.httpOptions);
  }
}