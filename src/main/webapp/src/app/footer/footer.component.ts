import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl} from '@angular/forms';
import { AlertService } from '../alert.service';
import { ServiceRequest } from '../serviceRequest';
declare var $: any;


@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  userForm: FormGroup;
  emailPattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
  constructor(private formBuilder: FormBuilder, private serviceRequest: ServiceRequest, private notifyService : AlertService) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
       name: ['', Validators.required],
       email: ['', [Validators.required, Validators.email, Validators.pattern(this.emailPattern)]],
       title: ['', Validators.required],
       message: ['', [Validators.required, Validators.minLength(20)]]
     });
  }

  sendMessage(){
    let self = this;
    let info = {
      content: self.userForm.value.message,
      pseudo: self.userForm.value.name,
      receiver: "packagemanager@gmail.com",
      sender: self.userForm.value.email,
      subject: self.userForm.value.title
    };

    this.serviceRequest.contactUs(info).subscribe(resp =>{
      if(resp.retCode > -1){
        self.notifyService.showSuccess(resp.retDescription,"");
        self.userForm.reset();
        $('#modalContact').modal('show');
      }
      
    })
  }
}
