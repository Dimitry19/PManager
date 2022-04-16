import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SharedConstants } from '.././SharedConstants';
import * as _ from 'underscore';
import { Router } from '@angular/router';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-search-all',
  templateUrl: './search-all.component.html',
  styleUrls: ['./search-all.component.css']
})
export class SearchAllComponent implements OnInit {

  lieux_depart;
  lieux_arrivee;
  dateDepart: number;
  dateArrive: number;
  departDate;
  arrivalDate;
  keyword = 'name';
  valueCategorie;
  valueTransport;
  villes = SharedConstants.Villes;
  Categories = SharedConstants.Categories;
  Transports = SharedConstants.Transports;
  
  public minDate = new DatePipe('en-US');
  public dateValue: Date;
  public minDate_e = new Date();
  public dateValue_e: Date;

  constructor(private router: Router, public formatter: NgbDateParserFormatter) { }

  ngOnInit(): void {
  }

  /* dates de depart et d’arrivee */
  depart(){
    if(this.dateValue){
      // console.log(this.minDate.transform(this.dateValue, 'dd/MM/yyyy hh:mm:ss'));
      this.departDate = this.dateValue.toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });

      //je transform la date en millisecondes
      this.dateDepart = this.dateValue.getTime();
    }
  }

  arrivee(){
    if(this.dateValue_e){
      this.arrivalDate = this.dateValue_e.toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
      this.dateArrive = this.dateValue_e.getTime();
    }
  }
  /* lieux de depart et d’arrivee */
  selectEvent(item) {
    this.lieux_depart = item;
 // do something with selected item
   }
   selectEventA(item) {
     this.lieux_arrivee = item;
  // do something with selected item
    }
   onChangeSearch(val: string) {
     // fetch remote data from here
     // And reassign the 'data' which is binded to 'data' property.
   }

   onFocused(e){
     // do something when input is focused
   }

   search(){
     
   var date1 = 0, date2 = 0, dep= "",arr= "", cat="", tra="";

   if (this.lieux_depart) {
     dep = this.lieux_depart.name;
   }
   if (this.lieux_arrivee) {
     arr = this.lieux_arrivee.name;
   }
   if (!this.dateDepart) {
     this.dateDepart = 0;
   }
   if (!this.dateArrive) {
     this.dateArrive = 0;
   }
   if(this.valueTransport){
    tra = this.valueTransport.value;
   }
   if(this.valueCategorie){
    cat = this.valueCategorie.value;
  }

     var filter = {
       "announceType": "",
       "description":"",
       "transport": tra,
       "category": cat,
       "price": "",
       "startDate": this.dateDepart,
       "endDate": this.dateArrive,
       "departure":dep,
       "arrival": arr,
       "user":"",
     "userId":"",
       "and":true
     };
     var filterString = JSON.stringify(filter);

      this.router.navigate(["/annonces",0], {queryParams:{filterString}});

   }

  //  tracbByFn(index,item){
  //   return index;
  // }

}
