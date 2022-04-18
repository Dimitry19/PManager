import { Component, OnInit} from '@angular/core';
import { CookieService } from 'ngx-cookie';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn} from '@angular/forms';
import { ServiceRequest, notif } from '../serviceRequest';
import { AlertService } from '.././alert.service';
import _ from 'underscore';
import { UsersUtils, SharedService } from '../SharedConstants';

declare var $: any;
// social network //prova pull
// import { SocialUser, AuthService } from "angularx-social-login";
// import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";

function myValidators (c: AbstractControl): {[key: string]: boolean} {
    if(!(c.value.match(/[0-9]/) && c.value.match(/[^a-z^A-Z^0-9]/) && c.value.match(/[A-Z]/) && c.value.length >= 8)){ 
      return {'test': true}
    }
    return null;
  }
function passwordConfirming (password): ValidatorFn{
  return (c: AbstractControl): {[key: string]: boolean } | null => {
    let confirmPassword = c.value;
    if (password !== confirmPassword) {
        var element = document.getElementsByName('confirmPassword');
        element.forEach(elt => {
          elt.className = elt.className.replace(/\bng-invalid\b/g, "displaystyle-warning");
        });

      return {"invalid": true};
    }
    else{
      var element = document.getElementsByName('confirmPassword');
      element.forEach(elt => {
        elt.className = elt.className.replace(/\bdisplaystyle-warning\b/g, "ng-valid");
      });
    }
    return null;
  }
}

@Component({
  selector: 'app-head',
  templateUrl: './head.component.html',
  styleUrls: ['./head.component.css']
})
export class HeadComponent implements OnInit {

  email: string;
  consenso1:boolean = false;
  consenso2:boolean = false;
  consenso3:boolean = false;
  code_regis;
  msg_regis:string;
  lock: boolean;
  timerId: any;
  connected: boolean = false;
  user: any;
  sharedService =  new SharedService;
  //for validation
  capital: boolean = false;
  number: boolean = false;
  specialCHAR: boolean = false;
  lenght: boolean = false;
  userForm: FormGroup;
  userFormR: FormGroup;
  userFormF: FormGroup;
  // pwd: any;
  notificationList: Array<notif> = [];

  //time: number = 1;
  loggedUser: any;
  emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";

  constructor(private router: Router, private formBuilder: FormBuilder, private notifyService : AlertService,
     private startup : ServiceRequest, private cookieService: CookieService) {

      let self = this;
      self.timerId = setInterval(() => {
        if(sessionStorage.loggedUser){
          self.loggedUser = JSON.parse(sessionStorage.loggedUser);
         
          self.notificationList = this.startup.notifications;  
        }      
      }, 500);
      
       $(document).ready(function () {
          	var currentGfgStep, nextGfgStep, previousGfgStep;
          	var opacity;
          	var current = 1;
          	var steps = $("fieldset").length;


          	setProgressBar(current);

          	$(".next-step").click(function () {

          		currentGfgStep = $(this).parent();
          		nextGfgStep = $(this).parent().next();

          		$("#progressbar li").eq($("fieldset")
          			.index(nextGfgStep)).addClass("active");

          		nextGfgStep.show();
          		currentGfgStep.animate({ opacity: 0 }, {
          			step: function (now) {
          				opacity = 1 - now;

          				currentGfgStep.css({
          					'display': 'none',
          					'position': 'relative'
          				});
          				nextGfgStep.css({ 'opacity': opacity });
          			},
          			duration: 500
          		});
          		setProgressBar(++current);
          	});

          	$(".previous-step").click(function () {

          		currentGfgStep = $(this).parent();
          		previousGfgStep = $(this).parent().prev();

          		$("#progressbar li").eq($("fieldset")
          			.index(currentGfgStep)).removeClass("active");

          		previousGfgStep.show();

          		currentGfgStep.animate({ opacity: 0 }, {
          			step: function (now) {
          				opacity = 1 - now;

          				currentGfgStep.css({
          					'display': 'none',
          					'position': 'relative'
          				});
          				previousGfgStep.css({ 'opacity': opacity });
          			},
          			duration: 500
          		});
          		setProgressBar(--current);
          	});

          	function setProgressBar(currentStep) {
          		var percent = (100 / steps) * current;
          		//percent = percent.toFixed();
          		$(".progress-bar")
          			.css("width", percent + "%")
          	}

          	$(".submit").click(function () {
          		return false;
          	})
          });
  }


  ngOnInit(): void {
    let self = this;           
     
    if(self.router.url.indexOf('index') > -1 || self.router.url === "/"){                 
      this.userForm = this.formBuilder.group({
        username: ['', [Validators.required]],
        // email: ['', [Validators.required, Validators.email, Validators.pattern(self.emailPattern)]],
        password: ['', [Validators.required]]
      });
      this.userFormR = this.formBuilder.group({
        gender: ['',[Validators.required]],
        firstName: ['', [Validators.required, Validators.minLength(5)]],
        lastName: ['', [Validators.required, Validators.minLength(5)]],
        username: ['', [Validators.required, Validators.minLength(3)]],
        phone: ['', [Validators.required, Validators.minLength(10),Validators.maxLength(10)]],
        email1: ['', [Validators.required, Validators.email, Validators.pattern(self.emailPattern)]],
        password1: ['', [Validators.required, myValidators]],
        confirmPassword: ['']
         }
         //{validator: passwordConfirming}
       );
       this.userFormF = this.formBuilder.group({
          email2: ['', [Validators.required, Validators.email, Validators.pattern(self.emailPattern)]]
        });
        
        this.userFormR.get('password1').valueChanges.subscribe(value => {
          // self.pwd = value;
          this.keyupPwd(value);
        });
        sessionStorage.clear();
        self.router.navigate(["/index"]);    
    }
    else{
      self.connected = !self.connected;    
      if(sessionStorage != undefined && sessionStorage.length >0){
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
      }
      
    }
    
  }
  checkR(item){
    //da implementare ....
  }
  showPWD(x){    
    if (x.type === "password") {
      this.lock = true;
      x.type = "text";
    } else {
      this.lock = false;
      x.type = "password";
    }
  }

  msg(ind: number){
    let self = this; 
    if(self.notificationList){
      
      //l'id de l'annonce commenté dans la notification.
      self.startup.notificationId(self.notificationList[ind].id).subscribe(val =>{ 
        if(val.type == "ANNOUNCE"){
          if(self.router.url.indexOf("annonce") > -1){
            this.router.navigateByUrl('#urlabsolu', { skipLocationChange: false }).then(() => {
              self.router.navigate(["/annonce",val.announceId,"NOTIFICATION"]); 
              // self.notificationList.splice(ind,1);
              self.startup.removeMessage(ind);
            }); 
          }else{
            self.router.navigate(["/annonce",val.announceId,"NOTIFICATION"]); 
            // self.notificationList.splice(ind,1);
            self.startup.removeMessage(ind);
          } 
        }else if(val.type == "USER"){
          self.router.navigate(["myaccount",{section:"review",id:ind}]);
          // self.notificationList.splice(ind,1);
        }
                                   
      });  
      
    }
  }

  loggedIn() {
   let self = this;
   
   self.user = UsersUtils.logUser(self.userForm.value.password,self.userForm.value.username);
  this.startup.isLoggedIn(self.user).subscribe(response =>{
    $('#modalLRForm').modal('hide'); 
    $('#modalLRFormR').modal('hide');
    if(response.retCode == 0){
      self.msg_regis = response.retDescription;
      sessionStorage.setItem('loggedUser',JSON.stringify(response));
      self.notifyService.showSuccess("Salut "+this.userForm.value.username.toUpperCase(),"");
       if (!sessionStorage.disclaimerModal) {
      setTimeout(function() {
          $('#disclaimerModal').modal({
            show: true,
            backdrop: 'static',
            keyboard: false
          });
      }, 1500);
      setTimeout(function() {
        $('#cookiesSideModalRightBottom').delay(1000).fadeIn();
        // $('#cookiesSideModalRightBottom').modal({
        //   show: true,
        // });
        sessionStorage.disclaimerModal = 1;
      }, 6000);
      
    }
      self.connected = !self.connected;
      self.userForm.reset();
      self.router.navigate(["/home"]);
      
  }
  else{
    self.notifyService.showError(response.details[0],"");
  //  this.spinner.hide();
  }
});
}

closeCookies(){
  $('#cookiesSideModalRightBottom').fadeOut();
}
  forgotPwd() {

  let email = {email: this.userFormF.value.email2};
  
  this.startup.forgotPwd(email).toPromise().then(response =>{
    if(response.retCode == 0){
      this.notifyService.showSuccess("Veillez consulter votre mail","");
      this.userFormF.reset();
    }
    else{
      this.notifyService.showError(response.message,"");
    }
   
  });

  }
  registerIn() {
   let self = this;
   self.user = UsersUtils.createUser(self.userFormR.value.email1,self.userFormR.value.firstName,self.userFormR.value.gender,
    self.userFormR.value.lastName,self.userFormR.value.password1,self.userFormR.value.phone,self.userFormR.value.username);
  this.startup.Registration(self.user).toPromise().then(response =>{
    //this.code_regis = response.retCode;
    this.msg_regis = response.retDescription;
    
  if(response.retCode == 0){
      $(document).ready(function () {
       $('button[data-toggle="modal"]').on('show.bs.tab', function (e) {
         //update progress
         $(e.target).attr('data-target','.modalDialogA');
       });
   });
     $('#modalLRFormR').modal('hide');
     $('#modalLRForm').modal('hide'); 
     $('#dialogA').modal('show');
     self.userFormR.reset();
     self.userForm.controls.password.setValue(null);
     self.userForm.controls.username.setValue(null);
     self.userForm.controls.password.markAsPristine({ onlySelf: true });
     self.userForm.controls.password.markAsUntouched({onlySelf: true});
     self.userForm.controls.username.markAsPristine({ onlySelf: true });
     self.userForm.controls.username.markAsUntouched({ onlySelf: true });
  }
  else{
    $('#modalLRFormR').modal('hide');
    $('#dialogb').modal('show');
    }
  });

  }

  navigate(type){

    if(this.router.url.indexOf('acheteur') > -1 && type !=='acheteur'){
      this.refreshPage(type);
    }
    if(this.router.url.indexOf('vendeur') > -1 && type !=='vendeur'){
      this.refreshPage(type);
    }

  }
  //refreshing
  refreshPage(type?) {
    let self = this;
    // this._document.defaultView.location.reload();
    //using relative path, i add 'some' to the attive path
    // this.router.navigate(['some'], {relativeTo:this.route});
    if(type){
       //using absolute path,etrangement qu'importe ce que je mets comme url ça fonctionne tjrs, prqw ?
      this.router.navigateByUrl('#urlabsolu', { skipLocationChange: false }).then(() => {
      this.router.navigate(["/addAnnonce", type]);
      });
    }
   
  }

  // Method to sign in with google.
    // singInGoogle(platform : string): void {
    //   let self = this;
    //   self.spinner.show();
    //   platform = GoogleLoginProvider.PROVIDER_ID;
    //   this.authService.signIn(platform).then(
    //     (response) => {
    //       console.log(platform + " logged in user data is= " , response);
    //       $(document).ready(function () {
    //
    //        $('button[data-toggle="modal"]').on('show.bs.tab', function (e) {
    //          console.log("ok");
    //          $(e.target).attr('data-target','.modalDialogA');
    //          $('#dialogA').modal('show');
    //          this.msg_regis = "Google In";
    //         });
    //       });
    //       let socialUser = {username: response.name, email: response.email, photo: response.photoUrl};
    //       this.router.navigate(["home",socialUser]);
    //       self.spinner.hide();
    //
    //     }
    //   );
    // }

  // Method to sign in with facebook ID FB:2613006052293888.
// signInFacebook(platform: string): void {
//   let self = this;
//   self.spinner.show();
//   platform = FacebookLoginProvider.PROVIDER_ID;
//   self.authService.signIn(platform).then(
//     (response) => {
//       console.log(platform + " logged in user data is= ", response);
//       let socialUser = {username: response.name, email: response.email, photo: response.photoUrl};
//       this.router.navigate(["home",socialUser]);
//       self.spinner.hide();
//     });
//
//   }

  // Method to log out.
  signOut(): void {
    let self = this;
    //self.authService.signOut();
    self.connected = !self.connected;
    //self.user = null;
    //this.time = 0;
    self.startup.islogout(self.userForm.value.username).subscribe(response =>{
      if(response.retCode != -1){         
        this.notifyService.showSuccess(response.retDescription,"");
        self.userForm.controls.password.setValue(null);
        self.userForm.controls.username.setValue(null);
        self.userForm.controls.password.markAsPristine({ onlySelf: true });
        self.userForm.controls.password.markAsUntouched({onlySelf: true});
        self.userForm.controls.username.markAsPristine({ onlySelf: true });
        self.userForm.controls.username.markAsUntouched({ onlySelf: true });
        clearInterval(self.timerId);
        this.startup.disconnectClicked();  
        sessionStorage.clear();
        self.loggedUser = null;
        self.router.navigate(['/']);          
      }else{
        this.notifyService.showError(response.message,"");
      }
    });
  }
 
  //for validation
  keyupPwd(value){
    let self = this;
      //for number
      if(value.match(/[0-9]/)){
       self.number = true;
      }
      else{
       self.number = false;
      }
      //for specialCHAR
      if(value.match(/[^a-z^A-Z^0-9]/)){
       self.specialCHAR = true;
      }
      else{
       self.specialCHAR = false;
      }
      //for capital
      if(value.match(/[A-Z]/)){
       self.capital = true;
      }
      else{
       self.capital = false;
      }
      //for length
      if(value.length >= 8){
       self.lenght = true;
      }
      else{
       self.lenght = false;
    }
    if(self.userFormR.get('password1').valid){
      self.userFormR.get('confirmPassword').setValidators(passwordConfirming(value));
      self.userFormR.get('confirmPassword').updateValueAndValidity;
    }
    
 }
 
 tracbByFn(index){
  return index;
}
}
