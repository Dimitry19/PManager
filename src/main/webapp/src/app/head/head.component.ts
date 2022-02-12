import { Component, OnInit, Input} from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn} from '@angular/forms';
import { AuthRequest, notif } from '.././auth-request';
import { NgxSpinnerService } from "ngx-spinner";
import { AlertService } from '.././alert.service';
import { timeout } from 'rxjs/operators';
import _ from 'underscore';

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

  user: string;
  email: string;
  consenso1:boolean = false;
  consenso2:boolean = false;
  consenso3:boolean = false;
  foto:String = null;
  code_regis;
  msg_regis:string;
  lock: boolean;
  timerId: any;
  connected: boolean = false;
  //for validation
  capital: boolean = false;
  number: boolean = false;
  specialCHAR: boolean = false;
  lenght: boolean = false;
  userForm: FormGroup;
  userFormR: FormGroup;
  userFormF: FormGroup;
  pwd: any;
  notificationList: Array<notif> = [];
  notifNumber: number = 0;
  //time: number = 1;
  loggedUser: any;
  constructor(private router: Router, private formBuilder: FormBuilder,
    private notifyService : AlertService,private spinner: NgxSpinnerService, 
    private startup : AuthRequest) {

      let self = this;
      self.timerId = setInterval(() => {
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.notifNumber = this.startup.notifications.length;
        self.notificationList = this.startup.notifications; 
        self.foto = 'data:'+self.loggedUser.image.origin+';base64,'+self.loggedUser.image.picByte;
      }, 1000);
      
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
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required]]
      });
      this.userFormR = this.formBuilder.group({
         firstName: ['', [Validators.required, Validators.minLength(5)]],
         lastName: ['', [Validators.required, Validators.minLength(5)]],
         username: ['', [Validators.required, Validators.minLength(3)]],
         phone: ['', [Validators.required, Validators.minLength(10),Validators.maxLength(10)]],
         email1: ['', [Validators.required, Validators.email]],
         password1: ['', [Validators.required, myValidators]],
         confirmPassword: ['']
         }
         //{validator: passwordConfirming}
       );
       this.userFormF = this.formBuilder.group({
          email2: ['', [Validators.required, Validators.email]]
        });
 
        this.userFormR.get('password1').valueChanges.subscribe(value => {
          self.pwd = value;
          this.keyupPwd(value);
        });
        self.router.navigate(["/index" ]);    
    }
    else{
      self.connected = !self.connected;    
      if(sessionStorage != undefined){
        self.loggedUser = JSON.parse(sessionStorage.loggedUser);
        self.user = self.loggedUser.username;      
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
    console.log("list: ", self.notificationList);    
    if(self.notificationList){
      //self.router.navigate(["/profile/",{section:'review', id: ind}]);
      //l'id de l'annonce commenté dans la notification.
      JSON.parse(sessionStorage.loggedUser).announces.forEach(element => {           
        element.messages.filter(elt =>{
          if(elt.id.id == self.notificationList[ind].id){
            self.startup.notificationId(elt.id.id).subscribe(val =>{ 
              if(self.router.url.indexOf("annonce") > -1){
                this.router.navigateByUrl('#urlabsolu', { skipLocationChange: false }).then(() => {
                  self.router.navigate(["/annonce",element.id ]); 
                }); 
              }else{
                self.router.navigate(["/annonce",element.id ]); 
              }                            
            });            
          }
        });
      
      });
      
    }
  }

  loggedIn() {
   let self = this;
  let user = {username: self.userForm.value.username, password: self.userForm.value.password, email: self.userForm.value.email, provider:"test",
  socialId:"test"};

  this.startup.isLoggedIn(user).subscribe(response =>{
    $('#modalLRForm').modal('hide'); 
    if(response.retCode == 0){
      self.msg_regis = response.retDescription;
      sessionStorage.setItem('loggedUser',JSON.stringify(response));
      self.notifyService.showSuccess("Salut "+this.userForm.value.username.toUpperCase(),"");
      self.connected = !self.connected;
      self.user = self.userForm.value.username;
      self.notifNumber = this.startup.notifications.length; 
      //take(this.time)
      if(response.image.origin != null && response.image.picByte != null)
          self.foto = 'data:'+response.image.origin+';base64,'+response.image.picByte;
      self.router.navigate(["/home"]);
      //this.spinner.hide();
  }
  else{
    self.notifyService.showError(response.details[0],"");
  //  this.spinner.hide();
  }
});
}

  forgotPwd() {

 /* $('#modalLRForm').modal('hide');
  $('#modalLRFormR').modal('hide');
  $('#modalForgot').modal('hide');
  $('body').removeClass('modal-open');
  $('.modal-backdrop').remove();*/
  let email = {email: this.userFormF.value.email2};

  this.startup.forgotPwd(email).toPromise().then(response =>{
    if(response.retCode == 0){
      this.notifyService.showSuccess("Veillez consulter votre mail","");
    }
    else{
      this.notifyService.showError("Adresse mail introuvable","");
    }
    // this.router.navigate(["home",{username: user.username}]);
    // this.spinner.hide();
  });

  }
  registerIn() {
    /*$('#modalLRForm').modal('hide');
    $('#modalLRFormR').modal('hide');
    $('body').removeClass('modal-open');
    $('.modal-backdrop').remove();*/

  let user = {username:this.userFormR.value.username, password: this.userFormR.value.password1, email: this.userFormR.value.email1, provider: "casa",
  socialId: "511744", firstName:this.userFormR.value.firstName, lastName:this.userFormR.value.lastName, phone: this.userFormR.value.phone, role:"USER", gender:"MALE"};
  this.startup.Registration(user).toPromise().then(response =>{
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
     $('#dialogA').modal('show');
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
    clearInterval(self.timerId);
    this.startup.disconnectClicked();  
    sessionStorage.clear();
    self.startup.islogout(self.user).subscribe(response =>{
      if(response.retCode != -1){         
        this.notifyService.showSuccess(response.retDescription,"");
        self.router.navigate(['/']);          
      }else{
        this.notifyService.showError("Une erreur est survenue ", "");
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
