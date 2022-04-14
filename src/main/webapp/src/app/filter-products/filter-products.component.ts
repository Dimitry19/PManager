import { DatePipe } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { SharedConstants } from '../SharedConstants';
import * as _ from 'underscore';
declare var $: any;

@Component({
  selector: 'app-filter-products',
  templateUrl: './filter-products.component.html',
  styleUrls: ['./filter-products.component.css']
})
export class FilterProductsComponent implements OnInit {

  keyword = 'name';
  villes = SharedConstants.Villes;
  Transports = SharedConstants.Transports;
  lieux_depart;
  lieux_arrivee;
  Categories = SharedConstants.Categories;
  public minDate = new DatePipe('en-US');
  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  topics: any = null;
  @Output() filters = new EventEmitter<any>();

  constructor(public formatter: NgbDateParserFormatter, private calendar: NgbCalendar,private route: ActivatedRoute,) {
       
    $(document).ready(function(){
      $("#formControlRange").slider();
      $('select').awselect();
      // $('#immersive_dropdown').awselect({
      //     background: "#1d456f",
      //     active_background: "rgb(135, 142, 144)",
      //     placeholder_color: "rgb(169, 232, 169)",
      //     placeholder_active_color: "rgb(169, 232, 169)",
      //     option_color: "#fff",
      //     vertical_padding: "15px",
      //     horizontal_padding: "20px",
      //     immersive: true
      //   });
     });


   }

  ngOnInit(): void {
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
  let self = this;
    var t = $("#immersive_dropdown").val();
    var c = $("#immersive_dropdown1").val();
    var p = $("#formControlRange").val();

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
      "transport": t,
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
    
    this.filters.emit(filter);
    
  }


  tracbByFn(index){
    return index;
  }


}
