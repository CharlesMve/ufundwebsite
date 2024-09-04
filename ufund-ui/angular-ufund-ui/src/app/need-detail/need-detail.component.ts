import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouteReuseStrategy, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { NeedService } from '../need.service';
import { LoginService } from '../shared/login.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrls: [ './need-detail.component.css' ]
})
export class NeedDetailComponent implements OnInit {
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private location: Location,
    private login: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.login.getIsAdmin() == false) {
      this.router.navigate(['/needs']);
    }
    this.getNeed();
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }
}