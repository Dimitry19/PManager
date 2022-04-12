import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl} from '@angular/forms';
import { ServiceRequest, notif } from '../serviceRequest';
import { AlertService } from '.././alert.service';
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";
// social network
// import { SocialUser, AuthService } from "angularx-social-login";
// import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import * as _ from 'underscore';
import { SharedService } from '../SharedConstants';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
declare var $: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
  catRes;
  noteRes;
  sexeUser = "MALE";
  current_datetime: Date;
  options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  birthday;
  updateUser: FormGroup;
  updateUserMDP: FormGroup;
  sharedService =  new SharedService;
  // .transform(new Date(), 'dd/MM/yyyy');
  check: boolean;
  location = "via parma 2021";
  annonces;
  annonceRes;
  annonceWithRes = [];
  countAnnWithRes = 0;
  dtOptions:any = {};
  titleModal: string;
  loggedUser;
  myReservations;
  id:string;
  readOnly: Boolean = true;
  disableEverything: boolean = false;
  views:boolean = false;
  source:string =null;
  viewTable:boolean = false;
  file : File;
  notification:string;
  notificationType:string;
  idAnnonce:number;
  notificationsList: Array<notif> = [];
  mesAbonnements: Array<any> = [];
  mesAbonnes: Array<any> = [];
  sectionToshow: string;
  @ViewChildren(DataTableDirective)
  //dtElement: DataTableDirective;
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1 : Subject<any> = new Subject();
  dtTrigger2 : Subject<any> = new Subject();


  constructor(private router: Router,private startup: ServiceRequest, private formBuilder: FormBuilder, private route: ActivatedRoute,
                          private notifyService : AlertService, private domSanitizer: DomSanitizer) {
      let self = this;  
      self.id = self.route.snapshot.paramMap.get('id');
      self.sectionToshow = self.route.snapshot.paramMap.get('section'); 
      if(sessionStorage.loggedUser){
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.notificationsList = this.startup.notifications;  
      } 
      setInterval(() => {
        if(sessionStorage.loggedUser){
          self.loggedUser = JSON.parse(sessionStorage.loggedUser);
          self.notificationsList = this.startup.notifications;  
        }      
      }, 500);

     
   }

  ngOnInit(): void {
    let self = this;
    
    self.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      processing: true,
      language: {
        "emptyTable": "Aucun record",
        "infoEmpty": "vide..",
        "search": "_INPUT_",
        "searchPlaceholder" : "Recherche",
        "zeroRecords": "Aucun record disponible",
        "info": "Page _PAGE_ de _PAGES_",
        "infoFiltered" : "(Filtré de _MAX_ record total)",
        "paginate" : {
          "first": "Premier",
          "previous": "Précédent",
          "next": "Suivant",
          "last": "Dernier"
        }
      }
    };

    if(sessionStorage != undefined){
      //sessionStorage.setItem('state',"myaccount");
      if(self.sectionToshow && self.id && self.sectionToshow === 'review'){
       
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.startup.annonceUserId(self.loggedUser.id).subscribe(response =>{
          if(response.retCode > -1){
            self.loggedUser.announces = response.results;  

            self.annonceReserve();
            self.annonces = self.loggedUser.announces;
          }else{
            self.notifyService.showError(response.details[0],"");
          }
          
          // sessionStorage.setItem('loggedUser',JSON.stringify(self.loggedUser));     
        });
        self.check = self.loggedUser.enableNotification;
  
        if(self.sharedService.checkImage(self.loggedUser)){
          self.source = 'data:'+self.loggedUser.image.origin+';base64,'+self.loggedUser.image.picByte;
        }
        
        self.startup.getReserveUser(self.loggedUser.id).subscribe(response =>{
          self.myReservations = response;
        },
        error =>{
          self.notifyService.showError(error,"")
        }
        );
        self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
          self.mesAbonnements = response.results;
        });
        self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
          self.mesAbonnes = response.results;
        });

        
        
        $(document).ready(function () {
          $('#tab'+self.id).click();
          
          if(self.notificationsList){
            self.notification = self.notificationsList[self.id].message;  
            self.notificationType = "USER";
          }
          
        });
        setTimeout(function() {
          self.msg(+self.id);
      }, 6000*10);
        

        self.updateUser = self.formBuilder.group({
          firstName: [self.loggedUser.firstName, [Validators.required]],
          lastName: [self.loggedUser.lastName, [Validators.required]],
          phone: [self.loggedUser.phone, [Validators.required]]
          }          
        );
   
       self.updateUserMDP = self.formBuilder.group({
          password: ['', [Validators.required]],
          password1: ['', [Validators.required]],
          password2: ['', [Validators.required]],
          },
          {validator: self.passwordConfirming}
        );
        

      }
      else if(!self.sectionToshow && self.id ){
        self.disableEverything = true;
        self.sectionToshow = 'personal';
       
        self.updateUserMDP = self.formBuilder.group({
          password: [{value:'', disabled: self.disableEverything},[Validators.required]],
          password1: [{value:'', disabled: self.disableEverything}, [Validators.required]],
          password2: [{value:'', disabled: self.disableEverything}, [Validators.required]],
          },
          {validator: self.passwordConfirming}
        );
        
        self.startup.userInfo(self.id).subscribe(response =>{
          self.loggedUser = response;          
          self.check = self.loggedUser.enableNotification;
          self.startup.annonceUserId(self.loggedUser.id).subscribe(response =>{
            if(response.retCode > -1){
              self.loggedUser.announces = response.results;  
              self.annonceReserve();
            self.annonces = self.loggedUser.announces;
            }else{
              self.notifyService.showError(response.details[0],"");
            }
            
            // sessionStorage.setItem('loggedUser',JSON.stringify(self.loggedUser));     
          });
          
          if(self.sharedService.checkImage(self.loggedUser)){
            self.source = 'data:'+self.loggedUser.image.origin+';base64,'+self.loggedUser.image.picByte;
          }
          
          self.startup.getReserveUser(self.loggedUser.id).subscribe(response =>{
            self.myReservations = response;
          },
          error =>{
            self.notifyService.showError(error,"")
          }
          );

          self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
            self.mesAbonnements = response?.results;
          });
          self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
            self.mesAbonnes = response?.results;
          });
         

          self.updateUser = self.formBuilder.group({
            firstName: [self.loggedUser?.firstName, [Validators.required]],
            lastName: [self.loggedUser?.lastName, [Validators.required]],            
            phone: [self.loggedUser?.phone, [Validators.required]]            
          }
          );
          
        });
         
      }
      else if(!self.sectionToshow && !self.id ){
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.startup.annonceUserId(self.loggedUser.id).subscribe(response =>{      
          if(response.retCode > -1){
            self.loggedUser.announces = response.results;  
            self.annonceReserve();
            self.annonces = self.loggedUser.announces;
          }else{
            self.notifyService.showError(response.details[0],"");
          }          
          // sessionStorage.setItem('loggedUser',JSON.stringify(self.loggedUser));     
        });
        self.sectionToshow = 'personal';

        self.check = self.loggedUser.enableNotification;
  
        if(self.sharedService.checkImage(self.loggedUser)){
          self.source = 'data:'+self.loggedUser.image.origin+';base64,'+self.loggedUser.image.picByte;
        }
        
        self.startup.getReserveUser(self.loggedUser.id).subscribe(response =>{
          self.myReservations = response;
        },
        error =>{
          self.notifyService.showError(error,"")
        }
        );

        self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
          self.mesAbonnements = response?.results;
        });
        self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
          self.mesAbonnes = response?.results;
        });
        
        self.updateUser = self.formBuilder.group({
          firstName: [self.loggedUser.firstName, [Validators.required]],
          lastName: [self.loggedUser.lastName, [Validators.required]],
          phone: [self.loggedUser.phone, [Validators.required]]
          }
        );
   
       self.updateUserMDP = self.formBuilder.group({
          password: ['', [Validators.required]],
          password1: ['', [Validators.required]],
          password2: ['', [Validators.required]],
          },
          {validator: self.passwordConfirming}
        );
        
      }
    }

  }

  passwordConfirming(c: AbstractControl): { invalid: boolean } {
   if (c.get('password1').value !== c.get('password2').value) {

       var element = document.getElementsByName('password2');
       element.forEach(elt => {
         elt.className = elt.className.replace(/\bng-valid\b/g, "displaystyle-warning");
       });

     return {invalid: true};
   }
   else{
     var element = document.getElementsByName('password2');
     element.forEach(elt => {
       elt.className = elt.className.replace(/\bdisplaystyle-warning\b/g, "ng-valid");
     });
   }
 }

toggleView(x){
  let self = this;
  if(self.viewTable){
    self.viewTable = false;
    x.innerHTML = "Tableau";
  }else{
    self.viewTable = true;
    x.innerHTML = "Carte";
  }
}
 annonceReserve(){
  let self = this;
  self.startup.getReserveFromUsers(self.loggedUser.id).subscribe(response=>{
    if(response.retCode != -1){
      self.annonceWithRes = response.results;
      self.countAnnWithRes = response.count;
    }else{
      self.notifyService.showError(response.details[0],"")
    }
    
  });
   
 }

 valide(id){
  let self = this;
   let val={
     "id":id,
     "validate": true
   };
   self.startup.validReserve(val).toPromise().then(response =>{
     if(response.retCode != -1){
       self.notifyService.showSuccess(response.retDescription,"");
       self.refreshPage();
     }else{
       self.notifyService.showError(response.message,"");
     }
   });
 }
 refus(id){
  let self = this;
   let val={
     "id":id,
     "validate": false
   };
   self.startup.validReserve(val).toPromise().then(response =>{
     if(response.retCode != -1){
       self.notifyService.showSuccess(response.retDescription,"");
       self.refreshPage();
     }else{
       self.notifyService.showError(response.message,"");
     }
   });

 }

  Modifier(){
    let self = this;
    self.readOnly = false;
    // self.current_datetime = null;
  }
  msg(ind:number){
    let self = this;    
    if(self.notificationsList){
      self.startup.notificationId(self.notificationsList[ind].id).subscribe(val =>{
        self.notification = val.message;  
        self.idAnnonce = val.announceId;
        self.notificationType = val.type;
        self.notificationsList.splice(ind,1);   
      });
    } 
  }

  Annuler(){
    let self = this;
      self.readOnly = true;
  }

  detail(id,source){
    let self = this;
    // let annonce = _.findWhere(self.annonces,{id:parseInt(id)});
    if(source == "NOTIFICATION"){
      self.startup.removeMessage(id);
    }
    self.router.navigate(["/annonce",id,source]);
  }

  mySexe(sexe){
    let self = this;
    self.sexeUser = sexe;
  }
  checked(item){
    let self = this;
    self.check = item.target.checked;
  }
  updSessions(userSession){
    let self = this;
    // let productsSession = JSON.parse(sessionStorage.products);
     let loggedUserTmp = JSON.parse(sessionStorage.loggedUser);

        // if(loggedUserTmp!=null){
        //   sessionStorage.setItem('loggedUser',JSON.stringify(userSession));
        //   self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        // }
  }

  Sauver(){
    let self = this;
      self.readOnly = true;

      var userupd={
      "id":self.loggedUser.id,
     "username":self.loggedUser.username,
     "email":self.loggedUser.email,
     "provider":"ijiigbbiiii",
     "socialId":"qwqaedd",
     "firstName":self.updateUser.value.firstName,
     "lastName":self.updateUser.value.lastName,
     "phone":self.updateUser.value.phone,
     "role":self.loggedUser.roles[0].description,
     "gender":self.sexeUser.toString()
   };

   self.startup.userUpd(userupd).toPromise().then(response=>{

     if(response.retCode != -1){
       self.notifyService.showSuccess("Le changement des données personelles a été effectué correctement","");
        // self.updSessions(response);
        self.loggedUser.firstName = self.updateUser.value.firstName;
        self.loggedUser.lastName = self.updateUser.value.lastName; 
        self.loggedUser.phone = self.updateUser.value.phone;
        self.refreshPage();
     }else{
       self.notifyService.showError(response.message,"");
       // self.refreshPage();
     }

   });

  }

  SauverMDP(){
    let self = this;
    self.readOnly = true;

      var userupd={
     "newPassword":self.updateUserMDP.value.password1,
     "oldPassword":self.updateUserMDP.value.password
   };

   self.startup.userUpdPwd(userupd,self.loggedUser.id).toPromise().then(response=>{

     if(response.retCode != -1){
       self.notifyService.showSuccess(response.retDescription,"");
        self.refreshPage();
     }else{
       self.notifyService.showError(response.details[0],"");
       // self.refreshPage();
     }

   });

  }

  notif(){
    let self = this;
    self.startup.userNotifications(self.check,self.loggedUser.id).toPromise().then(response =>{
      if(response.retCode != -1){
        sessionStorage.setItem('loggedUser',JSON.stringify(response));
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.check = self.loggedUser.enableNotification;
        self.refreshPage();
        self.notifyService.showSuccess(response.retDescription,"mis à jour");
      }else{
        self.notifyService.showError(response.message,"");
        // self.refreshPage();
      }
    });
  }

  birthdayChange(){
    let self = this;
      if(self.current_datetime){
        self.birthday = self.current_datetime.toLocaleDateString('fr-FR');
        //je transform la date en millisecondes
        let date = self.current_datetime.getTime();
      }
  }

  minus(){
    let self = this;
    self.annonceRes.announceInfo.remainWeight++;
    self.annonceRes.weight--;
  }

  plus(){
    let self = this;
    self.annonceRes.weight++;
    self.annonceRes.announceInfo.remainWeight--;
  }

  editRes(res,bool){
    let self = this;
    self.views = bool;
    if(bool){
      self.titleModal = "Detail de la reservation";
    }else{
      self.titleModal = "Modifier la reservation";
    }
    self.annonceRes = res;

    self.catRes = res.categories;
    self.noteRes = res.description;
    $("#modalUpdRes").modal("show");
  }

  updateRes(){
    let self = this;
    let cat1="", cat2="", cat3="", cat4="", cat5="";
    if($("#Checkbox0u").prop("checked") == true){
      cat1 = $("#Checkbox0u").val();
    }
    if($("#Checkbox1u").prop("checked") == true){
      cat2 = $("#Checkbox1u").val();
    }
    if($("#Checkbox2u").prop("checked") == true){
      cat3 = $("#Checkbox2u").val();
    }
    if($("#Checkbox3u").prop("checked") == true){
      cat4 = $("#Checkbox3u").val();
    }
    if($("#Checkbox4u").prop("checked") == true){
      cat5 = $("#Checkbox4u").val();
    }

    let res ={
      "announceId":	self.annonceRes.announceInfo.id,
      "categories":	[cat1,cat2,cat3,cat4,cat5],
      "description":	self.noteRes,
      "userId":	self.loggedUser.id,
      "weight": self.annonceRes.weight
    };

      self.startup.updateReserve(self.annonceRes.id,res).toPromise().then(response =>{
        if(response.retCode != -1){
          self.myReservations.results = _.filter(self.myReservations.results, item =>{
            return item.id != response.id;
          });
          self.myReservations.results.push(response);
          self.notifyService.showSuccess(response.retDescription,"");
          self.refreshPage();
        }else{
          self.notifyService.showError(response.message,"");
          // self.router.navigate(["/home"]);
        }
      });
  }

  delRes(res){
    let self = this;
    self.annonceRes = res;
    $("#modalDeleteRes").modal("show");
  }
  deleteRes(){
    let self = this;
    self.startup.deleteReserve(self.annonceRes.id).toPromise().then(response =>{
      if(response.retCode != -1){
        self.myReservations.results = _.filter(self.myReservations.results, item =>{
          return item.id != response.id;
        });
        self.notifyService.showSuccess(response.retDescription,"");
        self.refreshPage();
      }else{
        self.notifyService.showError(response.message,"");
        // self.refreshPage();
      }
    });
  }

  onImageChange(e){
    const reader = new FileReader();
    let self = this;
      if(e.target.files && e.target.files.length) {
             
         self.file = e.target.files[0];
        //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
       // const formdata: FormData = new FormData();
        //formdata.append('file', file,file.name);
        /*if(self.file.size * 2 > 2**20){
          self.notifyService.showError("L'image dépasse la taille maximale(1MB)","");
        }*/
          self.startup.usrImg(self.loggedUser.id,'USER',self.file).toPromise().then(response =>{
            //response = btoa(unescape(encodeURIComponent(response.toString())));                    
            if(response.retCode != -1){
              // console.log('Image uploaded successfully');           
             //self.sanitizeImageUrl('data:'+response.origin +';base64,'+ response.picByte).toString();
             self.source = 'data:'+response.origin +';base64,'+ response.picByte;           
             //next(self.source);
             self.loggedUser.image = {};
             self.loggedUser.image.picByte = response.picByte;
             self.loggedUser.image.origin = response.origin;
            //  let loggedUserTmp = self.loggedUser;
            //  sessionStorage.removeItem("loggedUser");
            //  if(loggedUserTmp){                        
            //   sessionStorage.setItem('loggedUser',JSON.stringify(loggedUserTmp));
            //  }           
             
          }else{
            self.notifyService.showError(response.message,"");
            // console.log('Image not uploaded successfully');
          }
        });
        
       
    }

}
refreshPage() {
  let self = this;
  //using absolute path,etrangement qu'importe ce que je mets comme url ça fonctionne tjrs, prqw ?
  self.router.navigateByUrl('/HeadComponent', { skipLocationChange: false }).then(() => {
  self.router.navigate(["/myaccount"]);
  });
}
refreshPageProfile(userId) {
  let self = this;
  //using absolute path,etrangement qu'importe ce que je mets comme url ça fonctionne tjrs, prqw ?
  self.router.navigateByUrl('', { skipLocationChange: false }).then(() => {
  self.router.navigate(['/profile/', userId]);
  });
}
sanitizeImageUrl(imageUrl: string): SafeUrl {
  let self = this;
    return self.domSanitizer.bypassSecurityTrustUrl(imageUrl);
}

unFollow(i){
  let self = this;
  let subscript = {
    "subscriberId": self.loggedUser.id,
    "subscriptionId": self.mesAbonnes[i].id
  };
  self.startup.unFollowUser(subscript).subscribe(response =>{
    
    if(response.retCode > -1){
      self.notifyService.showSuccess(response.retDescription,"");
      self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnements = response?.results;
      });
      self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnes = response?.results;
      });
    }
    else{
      self.notifyService.showError(response.message,"");
    }
  })
}

follow(i){
  let self = this;
  let subscript = {
    "subscriberId": self.loggedUser.id,
    "subscriptionId": self.mesAbonnes[i].id
  };
  self.startup.followUser(subscript).subscribe(response =>{
    
    if(response.retCode > -1){
      self.notifyService.showSuccess(response.retDescription,"");
      self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnements = response?.results;
      });
      self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnes = response?.results;
      });
    }
    else{
      self.notifyService.showError(response.message,"");
    }
  })
}

tracbByFn(index,item){
    return index;
}

find(i): boolean{
  let self = this;
  if(_.findWhere(self.mesAbonnements, {"id": self.mesAbonnes[i].id})){
    return true;
  }
  return false;
}
rerender(){
  let self = this;
  this.dtElements.forEach((dtElement: DataTableDirective) =>{
    if(dtElement.dtInstance){
      dtElement.dtInstance.then((dtInstance: DataTables.Api) =>{dtInstance.destroy()})
    }
  });
  this.dtTrigger1.next();
  this.dtTrigger2.next();
}
ngOnDestroy():void{
  let self = this;
  this.dtTrigger1.unsubscribe();
  this.dtTrigger2.unsubscribe();
}
}
