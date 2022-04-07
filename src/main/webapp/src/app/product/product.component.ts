import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ServiceRequest } from '../serviceRequest';
import { AlertService } from '.././alert.service';
import * as _ from 'underscore';
import { SharedConstants, SharedService } from '../sharedConstants';

declare var $: any;

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})


export class ProductComponent implements OnInit {

data = SharedConstants.Villes;

dataT = [
       {
         id: 1,
         name: 'Avion',
         value:'PLANE'
       },
       {
         id: 2,
         name: 'Voiture',
         value:'AUTO'
       },
       {
         id: 3,
         name: 'Bateau',
         value:'NAVE'
       }

    ];
erreurs = [];

keyword = 'name';
loggedUser;
foto: string;
id;
idRes;
userRes=1;
userResRead;
note: string = "";
userNote: string = "";
annonceCopie;
newcomment;
comment: string;
status;
send: boolean = false;
connected: boolean = false;
sharedService =  new SharedService;
annonce;
//notStar;
from:string ="";
stars: number;
display: number;
remainWeight: number;
reservations;
catRes: Array<any>;
catDiff: Array<any> = [];
follows:boolean = false;
// value = 1;
noModify:boolean = false;
hasreserve:boolean = false;
modifyC:boolean = false;
show:boolean = false;
readOnly:boolean = true;
readOnlyOwner:boolean = true;
public startDate: Date;
public endDate: Date;
//value categories
public valueCat: string[]=[];
// maps the local data column to fields property
public localFields: Object = { text: 'description', value: 'code' };
// set the placeholder to MultiSelect Dropdown input element
public localWaterMark: string = 'Selectionez les categories';

public categories: { [key: string]: Object; }[] = [
          {  code: 'Bijoux', description: 'Bijoux'},
          { code: 'Electronique', description: 'Electronique'},
          { code: 'Documents', description: 'Documents'},
          { code: 'Vetements', description: 'Vetements'},
          { code: 'Autres', description: 'Autres'},
      ];

  constructor(private router: Router,private startup : ServiceRequest, private route: ActivatedRoute, private notifyService : AlertService,
              ) {

      //this.id = this.route.snapshot.paramMap.get('id');
      this.route.paramMap.subscribe(url =>{
        this.id = url.get('id');
        this.from = url.get('notification');
      });
      //sessionStorage.setItem('state',"annonce;"+this.id);

}

  minus(){
    this.userRes--;
    this.remainWeight++;
  }

  plus(){
    this.userRes++;
    this.remainWeight--;
  }
  isCheck(){
    
    for (let c in [...Array(SharedConstants.Categories.length)]){
      if($("#Checkbox"+c).prop("checked")){
        return true;
      }
    }
    return false;
  }
  isCheckU(){
    
    for (let c in [...Array(SharedConstants.Categories.length)]){
      if($("#Checkbox"+c+"u").prop("checked")){
        return true;
      }
    }
    return false;
  }
  reservation(){
    let category = [...Array(SharedConstants.Categories.length)];
    console.log("category: ",category);
    
    for (let c in category){
      if($("#Checkbox"+c).prop("checked")){
        category[c] = $("#Checkbox"+c).val(); 
      }
    }

    let res ={
      "announceId":	this.annonce.id,
      "categories":	category,
      "description":	this.note,
      "userId":	this.loggedUser.id,
      "weight": this.userRes
    };

    this.startup.addReserve(res).toPromise().then(response =>{
      if(response.retCode != -1){
        this.notifyService.showSuccess(response.retDescription,"");
        this.note = "";
        this.refreshPage(this.id);
      }else{
        this.notifyService.showError(response.message,"");
        // this.router.navigate(["/home"]);
      }
    });

  }

  updateRes(){
    let category = [...Array(SharedConstants.Categories.length)];
    
    for (let c in category){
      if($("#Checkbox"+c+"u").prop("checked")){
        category[c] = $("#Checkbox"+c+"u").val(); 
      }
    }

    let res ={
      "announceId":	this.annonce.id,
      "categories":	category,
      "description":	this.userNote,
      "userId":	this.loggedUser.id,
      "weight": this.userRes
    };

      this.startup.updateReserve(this.idRes,res).toPromise().then(response =>{
        if(response.retCode != -1){
          this.notifyService.showSuccess(response.retDescription,"");
          this.refreshPage(this.id);
        }else{
          this.notifyService.showError(response.message,"");
          // this.router.navigate(["/home"]);
        }
      });
  }

  deleteRes(){
    this.startup.deleteReserve(this.idRes).toPromise().then(response =>{
      if(response.retCode != -1){
        this.notifyService.showSuccess(response.retDescription,"");
        this.hasreserve = false;
        this.refreshPage(this.id);
      }else{
        this.notifyService.showError(response.message,"");
        // this.router.navigate(["/home"]);
      }
    });
  }

  valide(id){
    let val={
      "id":id,
      "validate": true
    };
    this.startup.validReserve(val).toPromise().then(response =>{
      if(response.retCode != -1){
        this.notifyService.showSuccess(response.retDescription,"");
      }else{
        this.notifyService.showError(response.message,"");
      }
    });
  }
  refus(id){
    let val={
      "id":id,
      "validate": false
    };
    this.startup.validReserve(val).toPromise().then(response =>{
      if(response.retCode != -1){
        this.notifyService.showSuccess(response.retDescription,"");
      }else{
        this.notifyService.showError(response.message,"");
      }
    });

  }
  generateArrayOfNumbers(numbers) {
    return [...Array(numbers).keys()].slice();
  }

  //refreshing
  refreshPage(id) {
    //using absolute path,etrangement qu'importe ce que je mets comme url ça fonctionne tjrs, prqw ?
    this.router.navigateByUrl('#urlabsolu', { skipLocationChange: false }).then(() => {
    this.router.navigate(["/annonce", this.id,"OTHER"]);
    });
  }
  ngOnInit(): void {

    this.startup.annonceId(this.id,this.from).toPromise().then(response =>{
      if(response.retCode == -1){
        this.notifyService.showInfo("L'annonce n'a pas été retrouvé","");
        this.router.navigate(["/home"]);
      }
      this.annonce = response;
      this.startup.commentByAnnounce(this.id).toPromise().then(response =>{
        
        if(response.retCode > -1){
          this.annonce.messages = response.results;
          this.annonce.messages.sort();
          this.annonce.messages.reverse();
        }
        else{
          this.notifyService.showError(response.message,"");
        }
      });
      
      
      // je recupère les categories de l'annonce 
      _.each(this.annonce.categories, (item)=>{
        this.valueCat.push(item.description);
      });

      // je recupère le nombre de kilo prenotable
      this.remainWeight = response.remainWeight;
      this.stars = parseFloat(response.userInfo.rating);

      this.annonce.userInfo.dateCreated = response.userInfo.dateCreated.split(",")[0];

      this.foto = 'data:'+ this.annonce?.userInfo.origin +';base64,'+ this.annonce?.userInfo.picByte;

      if(sessionStorage != undefined){
          this.loggedUser = JSON.parse(sessionStorage.loggedUser);         
                              
          if(this.annonce.userInfo.id === this.loggedUser.id && !this.annonce.cancelled){
            this.show = true;
            this.readOnly = false;
          }
          
          this.startup.mesAbonnements(this.loggedUser.id).subscribe(response =>{            
            if(_.findWhere(response.results, {"id": this.annonce.userInfo.id})){
              this.follows = true;
            }
          })
          if (!sessionStorage.adModal && this.annonce.userInfo.id !== this.loggedUser.id) {
            setTimeout(function() {
              $('#admodal').find('.item').first().addClass('active');
                $('#admodal').modal({
                  backdrop: 'static',
                  keyboard: false
                });
            }, 30000);
            //   $('#adCarousel').carousel({
            //   interval: 4000,
            //   cycle: true
            // });
            //
            //   $("#buttonSuccess").click(function(e){
            //   	e.preventDefault();
            //   	var url = $(this).attr("href");
            //   	var win = window.open(url, '_blank');
            //   	$('#admodal').modal('hide');
            //   })
              sessionStorage.adModal = 1;
        }
          this.getReservation();
      }

    });

  }

  getReservation(){
    this.startup.getReserveAnn(this.id).toPromise().then(response=>{

      this.reservations = response.results;

      _.each(this.reservations, item=>{
        let count = 0;
        if(item['userInfo']['username'] === this.loggedUser.username.toString()){
            this.idRes = item.id;
            this.userRes = item.weight;
            this.userResRead = item.weight;
            this.userNote = item.description;
            this.status = item.validate;
            if(item.validate != 'INSERTED'){
              this.userRes = 1;
            }
            this.catRes = item.categories;
            this.hasreserve = true;

            //les categories qui ne sont pas dans la reservation
            this.annonce.categories.forEach(elt =>{
              let x = _.findWhere(this.catRes, {"code": elt.code});
              if(!x){
                this.catDiff.push(elt);
              }
            });
        }
        else if(!this.readOnly){
          if(item.validate == 'ACCEPTED'){
            count++;
          }
          if(count > 0){
            this.noModify = true;
            
          }
        }
      });

    });
  }

  updReservation(annonceSave){
    this.startup.getReserveAnn(this.id).toPromise().then(response=>{

      this.reservations = response.results;
      let bool = false;
      _.each(this.reservations, item=>{
          // je controlle si les categories de la reservation sont parmis celle de l'annonce

          // je retire - dans la liste des categories: en attendant les indications de DIMI
          item.categories = _.filter(item.categories, function(it) {
            return it.code != "-";
          });
          _.each(item.categories, (elt,index)=>{
            // si une categorie est presente dans la reservation et plus dans l'annonce
            let cats = _.findWhere(annonceSave.categories,{code: elt.code});
            if(cats == undefined){
              bool = true;
              item.categories.splice(index,1);
            }
          });

          if(bool){
            let category = [];
            _.each(item.categories, (e)=>{
              category.push(e.code);
            });
            let length = item.categories.length
            for(let i = 0; i<5-length; ++i){
              category.push("");
            }
            let res = {
              "announceId":	this.annonce.id,
              "categories":	category,
              "description": item.description,
              "userId":	this.loggedUser.id,
              "weight": item.weight
            };
            this.startup.updateReserve(item.id,res).toPromise().then(response=>{
              if(response.retCode == -1){
                this.notifyService.showError(response.message,"");
              }
            });
          }
      });

    });
  }
  updSessions(c){

      if(c !== 'D'){
        this.refreshPage(this.id);
      }
  }
  modify(){
    this.readOnlyOwner = false;

    this.startup.annonceId(this.annonce.id,this.from).toPromise().then(response =>{
      this.annonceCopie = response;
      // console.log(response);
    })
  }

  dateToMills(date){
    if(date != null){
      if(date.toString().indexOf('/') != -1){
        var tab = date.toString().split('/');
        
        var dateFormat= tab[2]+"-"+tab[1]+"-"+tab[0];
        console.log("date: ", dateFormat);
        date = new Date(tab[2],tab[1]-1,tab[0]);
        console.log("date: ", date);
        
        return new Date(date).getTime();
      }
      else{
        return new Date(date).getTime();
      }

    }

  }

  save(){
    this.erreurs = [];

    var dateA = 0, dateD = 0;
    // console.log(this.annonce);

    this.startDate = this.annonce.startDate;
    
    if(this.annonce.startDate && this.annonce.startDate.toString().indexOf("/") == -1){
      this.annonce.startDate = this.startDate.toLocaleDateString();
      dateD = this.dateToMills(this.annonce.startDate);
    }
    if(this.annonce.startDate && this.annonce.startDate.toString().indexOf("/") != -1){
      dateD = this.dateToMills(this.annonce.startDate);
      
    }

    this.endDate = this.annonce.endDate;
    
    if(this.annonce.endDate && this.annonce.endDate.toString().indexOf("/") == -1){
      this.annonce.endDate = this.endDate.toLocaleDateString();
      dateA = this.dateToMills(this.annonce.endDate);
      
    }
    if(this.annonce.endDate && this.annonce.endDate.toString().indexOf("/") != -1){
      dateA = this.dateToMills(this.annonce.endDate);
      
    }


    if(this.annonce.arrival == this.annonce.departure){
        this.erreurs.push("Ville de départ et d'arrivée sont identiques");

    }
    if(!this.annonce.startDate || !dateD){
      this.erreurs.push("Date de départ non valide");
    }
    if(!this.annonce.endDate || !dateA){
      this.erreurs.push("Date d'arrivée non valide");
    }
    if(dateA < dateD){
        this.erreurs.push("Date d'arrivée est anterieure à la date de départ");

    }
    if(!this.annonce.description || this.annonce.description.trim().length < 15){
      this.erreurs.push("Description absente ou trop courte");
    }
    if((this.annonce.price == '-' || !this.annonce.price) && this.annonce.price > 0){
      this.erreurs.push("Prix Classique non valide");
    }
    if((this.annonce.preniumPrice == '-' || !this.annonce.preniumPrice) && this.annonce.preniumPrice > 0){
      this.erreurs.push("Prix Prenium non valide");
    }
    if((this.annonce.goldPrice == '-' || !this.annonce.goldPrice) && this.annonce.goldPrice >0){
      this.erreurs.push("Prix Gold non valide");
    }

    if(!this.annonce.weight || this.annonce.weight == 0){
      this.erreurs.push("Nombre de kilo non valide");
    }

    if(!this.valueCat || this.valueCat.length == 0){
      this.erreurs.push("Categorie non valide");
    }



    if(this.erreurs.length > 0){
      $('#modalErreur').modal('show');
      this.readOnlyOwner = false;

    }
    else{
      this.readOnlyOwner = true;

      var annonceUpd ={
        "id":this.annonce.id,
        "announceType": this.annonce.announceType,
        "arrival": this.annonce.arrival,
        "departure": this.annonce.departure,
        "startDate": dateD,
        "endDate": dateA,
        "description":this.annonce.description,
        "price": this.annonce.price,
        "preniumPrice":this.annonce.preniumPrice,
        "goldPrice":this.annonce.goldPrice,
        "transport": this.annonce.transport,
        "userId": this.loggedUser.id,
        "weight": this.annonce.weight,
        "categories":this.valueCat
      };
      // console.log(annonceUpd);

      this.startup.annonceUpd(annonceUpd).toPromise().then(response =>{
        if(response.retCode == 0){
          this.notifyService.showSuccess("Votre annonce a été ajournée avec sucess","");
          this.updReservation(response);
          this.updSessions('U');
        }
        else{
          this.notifyService.showError(response.message,"");
        }
        // console.log(response);
      });
    }

  }
  delete(){
    this.startup.annonceDel(this.annonce.id).toPromise().then(response =>{
      if(response.retCode == 0){
        this.updSessions('D');
        this.router.navigate(["/home"]);
        this.notifyService.showSuccess(response.retDescription,"");
      }
      else{
        this.notifyService.showError(response.retDescription,"");
      }
    });
  }
  annul(){
    this.readOnlyOwner = true;
    this.startDate = this.annonce.startDate;
    if(this.annonce.startDate.toString().indexOf("/") == -1){
      this.annonce.startDate = this.startDate.toLocaleDateString();
    }

    this.endDate = this.annonce.endDate;
    if(this.annonce.endDate.toString().indexOf("/") == -1){
      this.annonce.endDate = this.endDate.toLocaleDateString();
    }
    this.annonce = this.annonceCopie;
  }
  /* lieux de depart et d’arrivee */
  selectEvent(item) {
 // do something with selected item
    this.annonce.departure = item.name;
   }
  selectEventA(item) {
// do something with selected item
  this.annonce.arrival = item.name;
  }

  selectEventT(item) {
  // do something with selected item
  let self = this;
    self.annonce.descriptionTransport = item.name;
    self.annonce.transport = item.value;
    }

  selectEventC(item) {
  // do something with selected item
  }

   onChangeSearch(val: string) {
     // fetch remote data from here
     // And reassign the 'data' which is binded to 'data' property.
   }

   onFocused(e){
     // do something when input is focused
   }

   addReview(review){
     // console.log(review);
     if(review !== '' || review != undefined){
       var comm ={
         "announceId":this.annonce.id,
         "username":this.loggedUser.username,
         "content":review
       };
       this.startup.addComment(comm).toPromise().then(response =>{

         if(response.retCode != -1){
           this.comment = null;
           this.newcomment = null;
           //this.annonce.messages.sort();
           this.annonce.messages.reverse();
           this.annonce.messages.push(response);          
           this.annonce.messages.sort();
           this.annonce.messages.reverse();
           //this.annonce.messages.sort((one, two) => (one > two ? -1 : 1));           
           this.updSessions('AM');
           this.notifyService.showSuccess(response.retDescription,"");
        }
      });
     }
     else{
       this.notifyService.showError("Votre commentaire n'est pas valide","");
     }

   }

   saveComment(idcomment, review){
    this.modifyC = false;
    let i = null;
    if(review !== '' || review != undefined){
       var comm ={
         "announceId":this.annonce.id,
         "username":this.loggedUser.username,
         "content":review
       };

       this.startup.updateComment(idcomment,comm).toPromise().then(response =>{
         if(response.retCode != -1){
           this.notifyService.showSuccess(response.retDescription,"");

              this.annonce.messages = _.filter(this.annonce.messages, (item,index) =>{
                if(item.id == idcomment){
                  i = index;
                }
                return item.id != idcomment;
              });
              this.annonce.messages.splice(i,0,response);
              this.updSessions('AM');
           this.newcomment = null;
           this.comment = null;
         }else{
           this.notifyService.showError(response.retDescription,"");
        }
     });
   }
 }

   updateComment(idcomment,i){
     this.display = i;
     this.modifyC = true;
     this.send = true;
     let msg = _.findWhere(this.annonce.messages,{id: idcomment});
     if(msg != undefined){
       this.newcomment = msg.content;
     }else{
       this.notifyService.showError("Message introuvable","");
     }
   }

   deleteComment(idcomment){
     this.startup.deleteComment(idcomment.id).toPromise().then(response =>{
       if(response.retCode != -1){
         this.annonce.messages = _.filter(this.annonce.messages, item =>{
           return item.id != idcomment;
         });
         this.updSessions('DM');
         this.notifyService.showSuccess(response.retDescription,"");
       }else{
         this.notifyService.showError(response.retDescription,"");
       }
     });

   }

   cancelComment(){
     this.modifyC = false;
     this.send = false;
     this.comment = null;
     this.newcomment = null;
   }

  commentaire (event: any){
      // console.log(event);
    // trim() n'est pas supporté par les versions recentes de IE
    if(event == undefined|| event.trim().length <= 1){
   this.comment = '';
   this.send = false;
    }else{
      this.send = true;
      this.comment = event;
      this.newcomment = event;

      // aller à la ligne
     //  $('input').keyup(function () {
     // // I'm assuming that 1 letter will expand the input by 10 pixels
     // var oneLetterWidth = 10;
     //
     // // I'm also assuming that input will resize when at least five characters
     // // are typed
     // var minCharacters = 40;
     // var len = $(this).val().length;
     //
     // if (len > minCharacters) {
     //     // increase width
     //     // $(this).width(len * oneLetterWidth);
     //     // $(this).height(len);
     //
     //     $(this).html("'value'<br>");
     // } else {
     //     // restore minimal width;
     //     $(this).height(30);
     // }
     //  });

    }
  }

  votes(vote:string){
    // comment eviter qu'un utlisateur vote une annonce plus d'une fois ?
    // quand et comment eliminer/ajourne le vote d'un utlisateur pour une annonce ?
    let self = this;
    let ratingObj = {
      "details": "announce note",
      "rating": vote,
      "ratingUserId": self.annonce.userInfo.id,
      "title": "rating",
      "userId": this.loggedUser.id
    };   
    self.startup.addRatingAnnounce(ratingObj).subscribe(response =>{
      console.log(response);
      if(response.retCode > -1){
        self.notifyService.showSuccess("Merci pour votre vote", "Rating");  
      }
      else{
        self.notifyService.showError(response.message,"");
      }      
    })
  }
  unFollow(){
    let self = this;
    let subscript = {
      "subscriberId": self.loggedUser.id,
      "subscriptionId": self.annonce.userInfo.id
    };
    self.startup.unFollowUser(subscript).subscribe(response =>{
      console.log("response: ", response);
      if(response.retCode > -1){
        self.notifyService.showSuccess(response.retDescription,"");
        self.follows = false;
      }
      else{
        self.notifyService.showError(response.message,"");
      }
      
    })
  }
  follow(){
    let self = this;
    let subscript = {
      "subscriberId": self.loggedUser.id,
      "subscriptionId": self.annonce.userInfo.id
    };
    self.startup.followUser(subscript).subscribe(response =>{
      console.log("response: ", response);
      if(response.retCode > -1){
        self.follows = true;
        self.notifyService.showSuccess(response.retDescription,"");
      }
      else{
        self.notifyService.showError(response.message,"");
      }
    })
  }
 
  profileUser(id){
    let self = this;
    console.log("id: ",id);
    if(id && id != null){
      self.router.navigate(["/profile",id]);
    }
    
  }

  tracbByFn(index,item){
    return index;
  }


}
