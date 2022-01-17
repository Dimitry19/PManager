import { Component, OnInit} from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { AuthRequest } from '.././auth-request';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { SharedConstants } from '.././SharedConstants';
import * as _ from 'underscore';
declare var $: any;
//@Injectable()

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
  username;
  loggedUser;
  email;
  phone;
  valueCategorie
  keyword = 'name';
  data = [
     {
       id: 1,
       name: 'MILAN'
     },
     {
       id: 2,
       name: 'BOLOGNE'
     },
     {
       id: 3,
       name: 'DOUALA'
     },
     {
       id: 4,
       name: 'YAOUNDE'
     },
     {
       id: 5,
       name: 'PARIS'
     }
  ];
  itemFiltres = ["transport","category","price","startDate","endDate","departure","arrival","weigth"];
  lieux_depart;
  lieux_arrivee;
  Categories = SharedConstants.Categories;
  public minDate = new DatePipe('en-US');
  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  topics: any = null;

  constructor(private router: Router,public formatter: NgbDateParserFormatter,private calendar: NgbCalendar, private startup : AuthRequest) {

    $('.modal-backdrop').remove();
    
    $(document).ready(function(){
      $("#formControlRange").slider();
      $('select').awselect();
     });

  }

  ngOnInit(): void {
    
    if(sessionStorage != undefined){
      this.loggedUser = JSON.parse(sessionStorage.loggedUser);
      this.username = this.loggedUser.username;
      this.email = this.loggedUser.email;
      this.phone = this.loggedUser.phone;      
    }
    this.startup.connectClicked(this.username);     
  }

 

  toggle(x){
    if(x.innerHTML.indexOf("Affiner") > -1){
      x.innerHTML = "Masquer la recherche";
    }else{
      x.innerHTML = "Affiner la recherche";
    }
  }

  /* dates de depart et d’arrivee */
  onDateSelection(date: NgbDate) {
      if (!this.fromDate && !this.toDate) {
        this.fromDate = date;
      } else if (this.fromDate && !this.toDate && date && date.after(this.fromDate)) {
        this.toDate = date;
      } else {
        this.toDate = null;
        this.fromDate = date;
      }
    }

    isHovered(date: NgbDate) {
      return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
    }

    isInside(date: NgbDate) {
      return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
    }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || (this.toDate && date.equals(this.toDate)) || this.isInside(date) || this.isHovered(date);
  }

  validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
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
onChangeSearch(e) {
    // fetch remote data from here
    // And reassign the 'data' which is binded to 'data' property.
  }

onFocused(e){
    // do something when input is focused
  }

clear(){
    this.fromDate = null;
    // console.log("test");
  }

clear1(){
      this.toDate = null;
    }
search(){
      // var t = $("#immersive_dropdown").val();
      var c = this.valueCategorie;
      var p = $("#formControlRange").val()

    var date1 = 0, date2 = 0, dep= "",arr= "";

    if (this.fromDate) {
      date1 = new Date(this.fromDate.month.toString()+"-"+this.fromDate.day.toString()+"-"+this.fromDate.year.toString()).getTime();
      //console.log(this.minDate.transform(date1.getTime(), 'dd/MM/yyyy hh:mm:ss'));
    }

    if (this.toDate) {
      date2 = new Date(this.toDate.month.toString()+"-"+this.toDate.day.toString()+"-"+this.toDate.year.toString()).getTime();
    }
    if (this.lieux_depart) {
      dep = this.lieux_depart.name;
    }

    if (this.lieux_arrivee) {
      arr = this.lieux_arrivee.name;
    }


      var filter = {
        "announceType": "",
        "description":"",
        "transport": "",
        "category": c,
        "price": p,
        "startDate": date1,
        "endDate": date2,
        "departure":dep,
        "arrival": arr,
        "user":"",
      "userId":"",
        "and":true
      };

      var filterString = JSON.stringify(filter);
      this.router.navigate(["/annonces/0"],{queryParams:{filterString:filterString}});

    }

tracbByFn(index){
    return index;
  }

}
