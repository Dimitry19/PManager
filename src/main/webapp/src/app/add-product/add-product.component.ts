import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthRequest } from '.././auth-request';
import { NgxSpinnerService } from "ngx-spinner";
import { AlertService } from '.././alert.service';

import _ from 'underscore';
declare var $: any;


@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

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
  transport;
  loggedUser;
  username;
  email;
  phone;
  productsSession;
  desc_ann;
  categories=["BIJOUX","VETEMENTS","DOCUMENTS","ELECTRONIQUE","AUTRES"];
  
  cat = ["","","","",""];
  kilo;
  compagnie;
  dateDepart: number;
  dateArrive: number;
  Depart;
  Arrive;
  lieuxDepart;
  lieuxArrive;
  dateD;
  dateA;
  prix_kg = 0;
  prix1_kg = 0;
  prix2_kg = 0;
  type;
  public minDate = new DatePipe('en-US');
  public dateValue: Date;
  public minDate_e = new Date();
  public dateValue_e: Date;

  //Local Variable defined
  // options={
  //   types: ['(cities)'],
  //   componentRestrictions:{
  //     country:["AU"]
  //   }
  // }

  // @ViewChild(HeadComponent) head:HeadComponent;


  constructor(private http: Http, private router: Router,private startup: AuthRequest,private notifyService : AlertService,
              private route: ActivatedRoute, private spinner: NgxSpinnerService) {

        this.type = this.route.snapshot.paramMap.get('type');

          // console.log(this.minDate.transform(now, 'dd/MM/yyyy'));
        if(this.type == "vendeur"){
          $(document).ready(function () {
            //   $('#description').on('input', function() {
            //   $(this)
            //     .width (50)
            //     .height(50)
            //     .width (this.scrollWidth)
            //     .height(this.scrollHeight);
            // });
            $('select').awselect();

         //Wizard
         $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
           //update progress
           var step = $(e.target).data('step');
           var percent = (parseInt(step) / 6) * 90;
           $('.progress-bar').css({width: percent + '%'});
           $('.progress-bar').text(percent + "%");
           // $('.progress-bar').text("Step " + step + " of 4");
           var $target = $(e.target);
           if ($target.parent().hasClass('disabled')) {
               return false;
           }
         });

         $('.nav-tabs li>.active').parent().css('background-color','#3bbcfd');
         $(".next").click(function (e) {

             var $active = $('.nav-tabs li>.active');
             //console.log($active);
             $active.parent().css('background-color','#3bbcfd');

             $active.parent().next().find('.nav-link').removeClass('disabled');

             $($active).parent().next().find('a[data-toggle="tab"]').click();

         });

         $(".prev-step").click(function (e) {
             var $active = $('.nav-tabs li>a.active');
             $($active).parent().prev().find('a[data-toggle="tab"]').click();
         });

     });
   }else{
     $(document).ready(function () {
       //   $('#description').on('input', function() {
       //   $(this)
       //     .width (50)
       //     .height(50)
       //     .width (this.scrollWidth)
       //     .height(this.scrollHeight);
       // });
       $('select').awselect();

    //Wizard
    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
      //update progress
      var step = $(e.target).data('step');
      var percent = (parseInt(step) / 5) * 100;
      $('.progress-bar').css({width: percent + '%'});
      $('.progress-bar').text(percent + "%");
      // $('.progress-bar').text("Step " + step + " of 4");
      var $target = $(e.target);
      if ($target.parent().hasClass('disabled')) {
          return false;
      }
    });

    $('.nav-tabs li>.active').parent().css('background-color','#3bbcfd');
    $(".next").click(function (e) {

        var $active = $('.nav-tabs li>.active');
        //console.log($active);
        $active.parent().css('background-color','#3bbcfd');

        $active.parent().next().find('.nav-link').removeClass('disabled');

        $($active).parent().next().find('a[data-toggle="tab"]').click();

    });

    $(".prev-step").click(function (e) {
        var $active = $('.nav-tabs li>a.active');
        $($active).parent().prev().find('a[data-toggle="tab"]').click();
    });

});
   }


}

  ngOnInit(): void {
    if(sessionStorage != undefined){
      this.loggedUser = JSON.parse(sessionStorage.loggedUser);
      this.username = this.loggedUser.username;
      this.email = this.loggedUser.email;
      this.phone = this.loggedUser.phone;
    }
  }
  ngAfterViewInit() {
    
}
  // ngAfterViewInit() {
  //   this.type = this.head.navigate();
  //   console.log(this.head.navigate()); // I am a child component!
  // }
  depart(){
    // console.log(this.dateValue.getTime());
    // console.log(this.dateValue.toLocaleString());
    // console.log(this.minDate.transform(this.dateValue, 'dd/MM/yyyy hh:mm:ss'));
    // console.log(this.dateValue.toLocaleDateString(undefined, this.options));
    if(this.dateValue){
      this.dateD = this.dateValue.toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });

      //je transform la date en millisecondes
      this.dateDepart = this.dateValue.getTime();
    }

  }
  arrivee(){

    if(this.dateValue_e){
      this.dateA = this.dateValue_e.toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
      this.dateArrive = this.dateValue_e.getTime();
    }
  }

  // categorie(choix){
  //   this.categ_choisi = choix.toUpperCase();
  // }

    /* lieux de depart et d’arrivee */
    selectEvent(item) {
   // do something with selected item
      this.lieuxDepart = item;
      this.Depart = item.name;
     }
     selectEventA(item) {
    // do something with selected item
      this.lieuxArrive = item;
      this.Arrive = item.name;
      }

     onChangeSearch(val: string) {
       // fetch remote data from here
       // And reassign the 'data' which is binded to 'data' property.
     }

     onFocused(e){
       // do something when input is focused
     }

     state1(){
       if(!this.lieuxArrive){
         this.Arrive = null;
       }
     }

     state(){
       if(!this.lieuxDepart){
         this.Depart = null;
       }
     }

     mode(mode: string){
       this.transport = mode;
       if(mode==="avion"){
         $("#myCompagnie").modal('show');
       }
     }
     comp(item){
      this.compagnie = item;
     }
     description(event: any){

       if(event == undefined|| event.trim().length < 15){
         $('#description').tooltip({content:'Inserer au moins 15 caractères',
         items:'textarea#description',
         show: "fold",
      });
      this.desc_ann = '';
       }else{
         this.desc_ann = event;
       }
     }

     updSessions(annonce){
       console.log(sessionStorage);

       let loggedUserTmp = JSON.parse(sessionStorage.loggedUser);
       if(loggedUserTmp!=null){
         loggedUserTmp.announces.push(annonce);
         sessionStorage.setItem('loggedUser',JSON.stringify(loggedUserTmp));
         this.loggedUser = JSON.parse(sessionStorage.loggedUser);
       }
       if(sessionStorage.length > 1 && sessionStorage.products){
          let productsSession = JSON.parse(sessionStorage.products);
          if(productsSession != null){ //je dois verifier s'il est deja passé au moins une fois par "voir annonces"
            productsSession.push(annonce);
              sessionStorage.setItem('products',JSON.stringify(productsSession));
              this.productsSession = JSON.parse(sessionStorage.products);
          }
       }

     }

     checked(item, i){    
       if(_.indexOf(this.cat, item) > -1){
          this.cat[i] = "";
       }else{
          this.cat[i] = this.categories[i];
       }
       
       
       
       
    }

     addAnnonce(){
       let type;
       if(this.transport === "avion"){
         this.transport = "PLANE";
       }
       if(this.transport === "voiture"){
         this.transport = "AUTO";
       }
       if(this.transport === "bateau"){
         this.transport = "NAVE";
       }
       if(this.type == 'acheteur'){
         this.type = "BUYER";
       }
       else{
         this.type = "SELLER";
       }


       var annonce = {
         "departure":this.lieuxDepart.name,
         "arrival":this.lieuxArrive.name,
         "startDate":this.dateDepart,
         "endDate":this.dateArrive,
        "transport":this.transport,
        "compagnie":"",
        "price": this.prix_kg,
        "preniumPrice":this.prix1_kg,
        "goldPrice":this.prix2_kg,
        // "categories":[this.cat1,this.cat2,this.cat3,this.cat4],
        "categories":this.cat,
         "weight":this.kilo,
         "announceType":this.type,
         "description":this.desc_ann,
         "userId":this.loggedUser.id
       };
       console.log(annonce);

       this.startup.annonceAdd(annonce).subscribe(response =>{
         //SUCCESS
         if(response.retCode == 0){
           this.updSessions(response);

           this.router.navigate(["home"]);
           this.notifyService.showSuccess("Votre annonce a été ajouté avec sucess","");
         }else{
           this.notifyService.showError(response.message,"");
           this.router.navigate(["/"]);
         }
       });
     }

     tracbByFn(index,item){
         return index;
       }

}
