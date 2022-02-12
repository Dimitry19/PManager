import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm, FormGroup, FormBuilder, Validators, FormControl, AbstractControl} from '@angular/forms';
@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  userForm: FormGroup;
  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
       name: ['', Validators.required],
       email: ['', [Validators.required, Validators.email]]
       // message: ['', Validators.required]
     });
  }

  sendMessage(){
    let user = {name: this.userForm.value.name, email: this.userForm.value.email};
  }
}
