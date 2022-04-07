import { Component, OnInit, ViewChild} from '@angular/core';
import { DatePipe } from '@angular/common';
import { ServiceRequest } from '../serviceRequest';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { SharedConstants } from '.././SharedConstants';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import * as _ from 'underscore';
declare var $: any;


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  loggedUser;
  valueCategorie
  keyword = 'name';
  data = SharedConstants.Villes;
  // itemFiltres = ["transport","category","price","startDate","endDate","departure","arrival","weigth"];
  lieux_depart;
  lieux_arrivee;
  Categories = SharedConstants.Categories;
  // public minDate = new DatePipe('en-US');
  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  topics: any = null;
  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer: ToastContainerDirective;

  constructor(private startup : ServiceRequest, private toastrService: ToastrService,) {

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

}
