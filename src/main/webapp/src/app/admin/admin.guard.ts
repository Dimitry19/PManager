import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AlertService } from '.././alert.service'

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private router: Router, private notifyService : AlertService){

  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if(sessionStorage?.loggedUser && JSON.parse(sessionStorage.loggedUser).username != undefined){           
        return true;
      }
      else{
        this.notifyService.showWarning("Veuillez vous connecter","Permission");
        return this.router.parseUrl("/index");
      }

  }

}
