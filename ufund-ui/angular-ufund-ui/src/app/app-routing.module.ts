import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { NeedsComponent } from './needs/needs.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { LoginRegisterComponent } from './login-register/login-register.component';
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: NeedDetailComponent },
  { path: 'needs', component: NeedsComponent },
  { path: 'login', component: LoginRegisterComponent },
  { path: 'logout', component: LogoutComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}