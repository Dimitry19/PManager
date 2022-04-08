import { Component, OnInit, ViewChild} from '@angular/core';
import { ServiceRequest } from '../serviceRequest';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import * as _ from 'underscore';
import { AlertService } from '../alert.service';
declare var $: any;


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  loggedUser;
  announceToClasse;
  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer: ToastContainerDirective;

  constructor(private startup : ServiceRequest, private toastrService: ToastrService,  private notifyService : AlertService) {

    $('.modal-backdrop').remove();
  }

  ngOnInit(): void {  
    this.toastrService.overlayContainer = this.toastContainer;  
    if(sessionStorage != undefined){  
      //sessionStorage.setItem('state',"home");    
      this.loggedUser = JSON.parse(sessionStorage.loggedUser);   
      this.startup.connectClicked(this.loggedUser.username); 
    }
       
  }

  toggle(x){
    // this.toastrService.success('<i class="fa fa-smile-o" aria-hidden="true"></i>',"",{ positionClass: 'inline-flex' });
    if(x.innerHTML.indexOf("Affiner") > -1){
      x.innerHTML = "Masquer la recherche";
    }else{
      x.innerHTML = "Affiner la recherche";
    }
  }
  filterAnnonces(filtres){
    let self = this;
    self.startup.announceFiltred(filtres).toPromise().then(response =>{
      if(response.count == undefined){
        self.notifyService.showError(response.message,"");
      }
      else{
        self.announceToClasse = response;
      }
      });
    // this.router.navigate(["/annonces/0"],{queryParams:{filterString:filtres}});
  }
 

}
