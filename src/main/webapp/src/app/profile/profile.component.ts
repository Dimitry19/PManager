import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl} from '@angular/forms';
import { AuthRequest, notif } from '.././auth-request';
import { AlertService } from '.././alert.service';
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";

// social network
// import { SocialUser, AuthService } from "angularx-social-login";
// import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import * as _ from 'underscore';
declare var $: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
  username;
  lastname;
  firstname;
  email;
  idUser;
  phone;
  catRes;
  noteRes;
  sexeUser = "MALE";
  current_datetime: Date;
  options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  birthday;
  updateUser: FormGroup;
  updateUserMDP: FormGroup;
  // .transform(new Date(), 'dd/MM/yyyy');
  check: boolean;
  location = "via parma 2021";
  annonces;
  annonceRes;
  annonceWithRes = [];
  countAnnWithRes = 0;
  dtOptions: DataTables.Settings = {};
  titleModal;
  loggedUser;
  myReservations;
  readOnly: Boolean = true;
  views:boolean = false;
  source:string =null;
  viewTable:boolean = false;
  file : File;
  notification:string;
  notificationsList: Array<notif> = [];
  mesAbonnements: Array<any> = [];
  mesAbonnes: Array<any> = [];
  sectionToshow: string;


  constructor(private router: Router,private startup: AuthRequest, private formBuilder: FormBuilder, private route: ActivatedRoute,
                          private notifyService : AlertService, private domSanitizer: DomSanitizer) {
      let self = this;      
      self.startup.notifications.forEach((val:notif) =>{                  
        self.notificationsList.push(val);
         // .toLocaleDateString('fr-FR', self.options);
      }); 
     
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
      self.loggedUser = JSON.parse(sessionStorage.loggedUser);
      self.check = self.loggedUser.enableNotification;
      self.username = self.loggedUser.username;
      self.email = self.loggedUser.email;
      self.phone = self.loggedUser.phone;
      self.lastname = self.loggedUser.lastName;
      self.firstname = self.loggedUser.firstName;
      self.source = 'data:'+self.loggedUser.image.origin+';base64,'+self.loggedUser.image.picByte;;
      self.idUser = self.loggedUser.id;
      self.startup.getReserveUser(self.loggedUser.id).subscribe(response =>{
        self.myReservations = response;
      },
      error =>{
        self.notifyService.showError(error,"")
      }
      );

      _.each(self.loggedUser.announces, (item, index) =>{
        self.startup.getReserveAnn(item.id).toPromise().then(response=>{
          self.loggedUser.announces[index]['reservations'] = response.count;
          if(response.count > 0){
            self.annonceReserve(response);
          }
        });
      });

      // _.each(self.loggedUser.announces, (item) =>{
      //   console.log(item.reservations);
      //   self.countAnnWithRes += item['reservations'];
      // });
      self.annonces = self.loggedUser.announces;
      self.username.toUpperCase();

      self.startup.mesAbonnements(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnements = response.results;
      })
      self.startup.mesAbonnes(self.loggedUser.id).subscribe(response =>{
        self.mesAbonnes = response.results;
      })
    }

    self.updateUser = self.formBuilder.group({
       firstName: [self.firstname, Validators.required],
       lastName: [self.lastname, Validators.required],
       // username: ['', Validators.required],
       phone: [self.phone, Validators.required]
       // email: [self.email, [Validators.required, Validators.email]],
       // password: ['', [Validators.required]],
       // password1: ['', [Validators.required]],
       // password2: ['', [Validators.required]],
       },
       // {validator: self.passwordConfirming}
     );

    self.updateUserMDP = self.formBuilder.group({
       password: ['', [Validators.required]],
       password1: ['', [Validators.required]],
       password2: ['', [Validators.required]],
       },
       {validator: self.passwordConfirming}
     );
    
     self.sectionToshow = self.route.snapshot.paramMap.get('section');
      if(self.sectionToshow === 'review'){                    
        self.msg(+self.route.snapshot.paramMap.get('id'));
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
 annonceReserve(response){
  let self = this;
   self.annonceWithRes.push(response.results);
   self.countAnnWithRes += response.count;
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
       self.notifyService.showError("Une erreur s'est produite durant la validation","");
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
       self.notifyService.showError("Une erreur s'est produite durant le refus","");
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
        self.notification = self.notificationsList[ind].message;     
      });
    } 
  }

  Annuler(){
    let self = this;
      self.readOnly = true;
  }

  detail(id){
    let self = this;
    let annonce = _.findWhere(self.annonces,{id:parseInt(id)});    
    self.router.navigate(["/annonce",id]);
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

        if(loggedUserTmp!=null){
          sessionStorage.setItem('loggedUser',JSON.stringify(userSession));
          self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        }
  }

  Sauver(){
    let self = this;
      self.readOnly = true;

      var userupd={
      "id":self.loggedUser.id,
     "username":self.loggedUser.username,
     "email":self.email,
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
        self.updSessions(response);
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
          self.notifyService.showError("Une erreur s'est produite durant la reservation","");
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

        self.startup.usrImg(self.loggedUser.id,'USER',self.file).toPromise().then(response =>{
          //response = btoa(unescape(encodeURIComponent(response.toString())));                    
          if(response.retCode != -1){
            console.log('Image uploaded successfully');           
           //self.sanitizeImageUrl('data:'+response.origin +';base64,'+ response.picByte).toString();
           self.source = 'data:'+response.origin +';base64,'+ response.picByte;           
           //next(self.source);
           self.loggedUser.image.picByte = response.picByte;
           self.loggedUser.image.origin = response.origin;
           let loggedUserTmp = self.loggedUser;
           sessionStorage.removeItem("loggedUser");
           if(loggedUserTmp){                        
            sessionStorage.setItem('loggedUser',JSON.stringify(loggedUserTmp));
           }           
           
        }else{
          self.notifyService.showError(response.message,"");
          console.log('Image not uploaded successfully');
        }
      });
    }

}
refreshPage() {
  let self = this;
  //using absolute path,etrangement qu'importe ce que je mets comme url ça fonctionne tjrs, prqw ?
  self.router.navigateByUrl('/HeadComponent', { skipLocationChange: false }).then(() => {
  self.router.navigate(["/profile",{section:'personal'}]);
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
    }
    else{
      self.notifyService.showError("Une erreur est survenue","");
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
    }
    else{
      self.notifyService.showError("Une erreur est survenue","");
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
}
