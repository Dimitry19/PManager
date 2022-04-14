import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { ServiceRequest } from '../serviceRequest';
import { AlertService } from '.././alert.service';
import { SharedConstants, SharedService } from '.././SharedConstants';
// import { NgxPaginationModule } from 'ngx-pagination';
import * as _ from 'underscore';



@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  keyword = 'name';
  itemFiltres = [
    {
        id:1,
        name:"Transport",
        value:"transport"
    },
    {
        id:2,
        name:"Prix",
        value:"price"
    },
    {
        id:3,
        name:"Categorie",
        value:"category"
    },
    {
        id:4,
        name:"Date de Départ",
        value:"startDate"
    },
    {
        id:5,
        name:"Date d'Arrivée",
        value:"endDate"
    },
    {
        id:6,
        name:"Lieu de Départ",
        value:"departure"
    },
    {
        id:7,
        name:"Lieu d'Arrivée",
        value:"arrival"
    },
    {
        id:8,
        name:"Kilos",
        value:"weigth"
    }
    ];
  page: number = -1;
  lists = [];
  products;
  filtres = null;
  lieux_depart;
  lieux_arrivee;
  tag: string;
  loggedUser: object;
  sharedService =  new SharedService;
  public minDate = new DatePipe('en-US');
  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  villes = SharedConstants.Villes;
  Categories = SharedConstants.Categories;
  Transports = SharedConstants.Transports;
  // date: { year: number, month: number, day: number };


  constructor(private router: Router,private startup: ServiceRequest, private route: ActivatedRoute,
     private calendar: NgbCalendar, public formatter: NgbDateParserFormatter,
    private notifyService : AlertService) {

       if(sessionStorage.loggedUser != undefined){
         this.loggedUser = JSON.parse(sessionStorage.loggedUser);
       }       
       this.page = parseInt(this.route.snapshot.paramMap.get('page'));
       this.route.queryParamMap.subscribe(val =>{
         let filtres = val.get('filterString');
         if(filtres){
          this.filterAnnonces(JSON.parse(filtres));
         }else{
          this.allAnnonces(this.page);
         }
         
         
       })
       

  }

  ngOnInit(): void {
    
  }
  pages(page){
    // console.log(page);
    var id = page;
    this.allAnnonces(id);
    this.checkPreviousAndNext(id);
    var d = (page-1).toString();
    $(".page-link").removeClass("active");
    $("#id"+ d).addClass("active");
  }
  checkPreviousAndNext(id){
    // this.page = parseInt(this.route.snapshot.paramMap.get('page'));
    // console.log(id);
    if(id> 0){
      $("#prev").removeClass("disabled");
    }
    if(id == 0){
      $("#prev").addClass("disabled");
    }
    if(id < this.lists.length){
      $("#netx").removeClass("disabled");
    }
    if(id == this.lists.length){
      $("#next").addClass("disabled");
    }
  }
  next(){
    let self = this;

    if(self.page >= 0){
      this.page = this.page + 1;
      var id = this.page;      
      this.allAnnonces(id);
      this.checkPreviousAndNext(id);
      var d = (id-1).toString();
      $(".page-link").removeClass("active");
      $("#id"+ d).addClass("active");
    }
  
  }
  previous(){
    let self = this;

    if(self.page > 0){
      var id = this.page = this.page - 1;
      this.allAnnonces(id);
      this.checkPreviousAndNext(id);
      var d = (id-1).toString();
      $(".page-link").removeClass("active");
      $("#id"+ d).addClass("active");
    }
   
  }
  detail(id){
    this.router.navigate(["/annonce",id,"OTHER"]);
  }
  filterAnnonces(filtres){
    this.startup.announceFiltred(filtres).toPromise().then(response =>{
     if(response.count == undefined){
       this.notifyService.showError(response.message,"");
       this.router.navigate(["/"]);
     }
     else{
       this.pagination(response);
     }
     });
  }
  allAnnonces(id){
    this.startup.annoncesList(id,"OTHER").toPromise().then(response =>{
         
      if(response.count == undefined){
        this.notifyService.showError(response.message,"");
        this.router.navigate(["/"]);
      }
      else{
      response.results = _.sortBy(response.results,"lastUpdated");
        this.pagination(response);
        this.router.navigate(["/annonces",id]);
      }
    });
  }
  generateArrayOfNumbers(numbers) {
    return [...Array(numbers).keys()].slice();
  }

  pagination(annonces){

    var count = Math.round(annonces.count/12);

    if(count <= 1){
      $("#pagination").hide();
    }
    else{
      this.lists = this.generateArrayOfNumbers(count-1);
    }
    if(annonces.results.length > 0){
      this.products = annonces.results;
    }
    else{
      this.notifyService.showInfo("Aucune annonce n'a été trouvé pour cette recherche!","");
    }

  }
  filter(item){
    this.products = _.sortBy(this.products,item.value);
    this.tag = item.name.toUpperCase();
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
     onChangeSearch(item) {

       // fetch remote data from here
       // And reassign the 'data' which is binded to 'data' property.
     }

     onFocused(item){

       // do something when input is focused
     }

     clear(){
       this.fromDate = null;
     }

     clear1(){
       this.toDate = null;
     }

     tracbByFn(index){
       return index;
     }

}
