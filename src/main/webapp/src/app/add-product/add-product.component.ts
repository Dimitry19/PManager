import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute, RoutesRecognized } from '@angular/router';
import { ServiceRequest } from '../serviceRequest';
import { AlertService } from '.././alert.service';
import { Annonce, SharedConstants, SharedService, UsersUtils } from '../sharedConstants';
import _ from 'underscore';
import { filter, pairwise } from 'rxjs/operators';
declare var $: any;


@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  keyword = 'name';
  transport;
  loggedUser;
  username;
  email;
  phone;
  productsSession;
  desc_ann;
  shared = new SharedService()
  categories = SharedConstants.Categories;
  data = SharedConstants.Villes;
  cat: Array<string> = []; 
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
  // public minDate = new DatePipe('en-US');
  public dateValue: Date;
  public minDate_e = new Date();
  public dateValue_e: Date;
  annonce: Annonce;
  values: Array<boolean> = [false,false,false,false,false];
  //Local Variable defined
  // options={
  //   types: ['(cities)'],
  //   componentRestrictions:{
  //     country:["AU"]
  //   }
  // }
  constructor( private router: Router,private startup: ServiceRequest,private notifyService : AlertService,
              private route: ActivatedRoute ) {

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

         $('.nav-tabs li>.active').parent().css('background-color','#42C2FF');
         $(".next").click(function (e) {

             var $active = $('.nav-tabs li>.active');
             
             $active.parent().css('background-color','#42C2FF');

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

            $('.nav-tabs li>.active').parent().css('background-color','#42C2FF');
            $(".next").click(function (e) {

                var $active = $('.nav-tabs li>.active');
                
                $active.parent().css('background-color','#42C2FF');

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
    let self = this;
    if(sessionStorage != undefined){
      //sessionStorage.setItem('state',"addAnnonce;"+self.type);
      self.loggedUser = JSON.parse(sessionStorage.loggedUser);
      self.username = self.loggedUser.username;
      self.email = self.loggedUser.email;
      self.phone = self.loggedUser.phone;
    }
  }

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
  deselected(val: string){    
    this.Depart = null;
  }
  selectEventA(item) {
// do something with selected item
  this.lieuxArrive = item;
  this.Arrive = item.name;
  }
  deselectedA(val: string){
    this.Arrive = null;
  }
  onChangeSearch(val: string) {
    // fetch remote data from here
    // And reassign the 'data' which is binded to 'data' property.
  }

  onFocused(e){
    // do something when input is focused
  }
 
  mode(mode: string){
      this.transport = mode;
    //  if(mode==="avion"){
    //    $("#myCompagnie").modal('show');
    //  }
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

       let loggedUserTmp = JSON.parse(sessionStorage.loggedUser);
       if(loggedUserTmp!=null){
         loggedUserTmp.announces.push(annonce);
        //  sessionStorage.setItem('loggedUser',JSON.stringify(loggedUserTmp));
         this.loggedUser = JSON.parse(sessionStorage.loggedUser);
       }
       if(sessionStorage.length > 1 && sessionStorage.products){
          let productsSession = JSON.parse(sessionStorage.products);
          if(productsSession != null){ //je dois verifier s'il est deja passé au moins une fois par "voir annonces"
            productsSession.push(annonce);
              // sessionStorage.setItem('products',JSON.stringify(productsSession));
              this.productsSession = JSON.parse(sessionStorage.products);
          }
       }

     }

    categs(item,t){           
       if(_.indexOf(this.cat, item) > -1){
          this.cat.splice(_.indexOf(this.cat, item),1);
          this.values[t] = false;         
       }else{
          this.cat.push(item);
          this.values[t] = true;       
       }
    }

    addAnnonce(){
       let self = this;
       switch (self.transport){
         case "avion": 
          self.transport = "PLANE";
            break;
          case "voiture":
            self.transport = "AUTO";
            break;
          case "bateau":
            self.transport = "NAVE";
            break;
       };

       switch (self.type){
        case "acheteur": 
          self.type = "BUYER";
          break;
        case "vendeur":
          self.type = "SELLER";
          break;
      };

      self.annonce = UsersUtils.createAnnounce(self.type,self.lieuxArrive.name,self.cat,self.lieuxDepart.name,self.desc_ann,
        self.dateArrive,self.prix2_kg,self.prix1_kg,self.prix_kg,self.dateDepart,self.transport,self.loggedUser.id,self.kilo);

       this.startup.annonceAdd(self.annonce).subscribe(response =>{
         //SUCCESS
         if(response.retCode == 0){
          //  this.updSessions(response);
           this.router.navigate(["home"]);
           this.notifyService.showSuccess(response.retDescription,"");
         }else{
           this.notifyService.showError(response.message,"");
          //  this.router.navigate(["/"]);
         }
       });
     }

    tracbByFn(index,item){
         return index;
       }
    cancel(){
      $("#backOnPreview").modal({show:true, backdrop: 'static', keyboard: false})
    }
    backOnPreview(){
      this.router.events.pipe(filter((evt: any) => evt instanceof RoutesRecognized), pairwise()).subscribe((events: RoutesRecognized[]) => {
          // console.log('previous url', events[0].urlAfterRedirects);
          // console.log('current url', events[1].urlAfterRedirects);
          if(events[0].urlAfterRedirects.indexOf("index") > -1){
            this.router.navigate([events[0].urlAfterRedirects]);
          }
        });
        
    }
    
}
