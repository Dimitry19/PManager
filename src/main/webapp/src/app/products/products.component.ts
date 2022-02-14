import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { DatePipe } from '@angular/common';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthRequest } from '.././auth-request';
import { NgxSpinnerService } from "ngx-spinner";
import { AlertService } from '.././alert.service';
import { SharedConstants } from '.././SharedConstants';
// import { NgxPaginationModule } from 'ngx-pagination';
import * as _ from 'underscore';
declare var $: any;


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  keyword = 'name';
  email = "";
  phone;
  villes;
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
  username = " ";
  page;
  lists = [];
  products;
  filtres = null;
  lieux_depart;
  lieux_arrivee;
  connected = false;
  tag;
  idUser = "";
  Categories;
  Transports;
  loggedUser;
  public minDate = new DatePipe('en-US');
  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  // date: { year: number, month: number, day: number };


  constructor(private http: Http, private router: Router,private startup: AuthRequest, private route: ActivatedRoute,
     private spinner: NgxSpinnerService, private calendar: NgbCalendar, public formatter: NgbDateParserFormatter,
    private notifyService : AlertService) {

       // this.fromDate = calendar.getToday();
       // this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
       this.villes = SharedConstants.Villes;
       this.Categories = SharedConstants.Categories;
       this.Transports = SharedConstants.Transports;
       if(sessionStorage.loggedUser != undefined){
         this.connected = true;
         this.loggedUser = JSON.parse(sessionStorage.loggedUser);
         this.username = this.loggedUser.username;
         this.email = this.loggedUser.email;
         this.phone = this.loggedUser.phone;
         this.idUser = this.loggedUser.id;
       }

       this.filtres = this.route.snapshot.queryParamMap.get('filterString');
       // console.log(this.filtres);
       if(this.filtres){
         var filtres = JSON.parse(this.filtres);
         // console.log(filtres);
         this.filterAnnonces(filtres);
       }else{
         this.page = parseInt(this.route.snapshot.paramMap.get('page'));
         // console.log(this.page);
         this.allAnnonces(this.page);
       }



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
    this.page = parseInt(this.route.snapshot.paramMap.get('page'));
    var id = this.page + 1;
    this.allAnnonces(id);
    this.checkPreviousAndNext(id);
    var d = (id-1).toString();
    $(".page-link").removeClass("active");
    $("#id"+ d).addClass("active");
  }
  previous(){
    this.page = parseInt(this.route.snapshot.paramMap.get('page'));
    var id = this.page - 1;
    this.allAnnonces(id);
    this.checkPreviousAndNext(id);
    var d = (id-1).toString();
    $(".page-link").removeClass("active");
    $("#id"+ d).addClass("active");
  }
  detail(id){
    this.router.navigate(["/annonce",id]);
  }
  filterAnnonces(filtres){
    this.startup.filter(filtres).toPromise().then(response =>{
     sessionStorage.setItem('products',JSON.stringify(response.results));
     // this.pagination(response);
     if(response.count == undefined){
       this.notifyService.showError("Une erreur est survenue","");
       this.router.navigate(["/"]);
     }
     else{
       this.pagination(response);
     }
     });
  }
  allAnnonces(id){
    this.startup.annoncesList(id).toPromise().then(response =>{
      console.log(response);
      sessionStorage.setItem('products',JSON.stringify(response.results));
      // console.log(sessionStorage);
      if(response.count == undefined){
        this.notifyService.showError("Une erreur est survenue","");
        this.router.navigate(["/"]);
      }
      else{
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

  search(){
    var t = $("#immersive_dropdown").val();
    var c = $("#immersive_dropdown1").val();
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
      "transport": t,
      "category": c,
      "price": p,
      "startDate": date1,
      "endDate": date2,
      "departure":dep,
      "arrival": arr,
      "user":this.email,
    "userId":this.idUser,
      "and":true
    };


    this.startup.filter(filter).toPromise().then(response =>{
      //SUCCESS
      if(response.count > 0){
        this.pagination(response);
      }else{
        this.notifyService.showInfo("Aucune annonce n'a été trouvé pour cette recherche!","");
        // this.pagination(response);
      }
      // console.log(response);
    });




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
     onChangeSearch(val: string) {

       // fetch remote data from here
       // And reassign the 'data' which is binded to 'data' property.
     }

     onFocused(e){

       // do something when input is focused
     }

     clear(){
       this.fromDate = null;
     }

     clear1(){
       this.toDate = null;
     }

     tracbByFn(index,item){
       return index;
     }

}
