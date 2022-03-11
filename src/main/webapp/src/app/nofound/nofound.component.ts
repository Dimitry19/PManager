import { Component, OnInit } from '@angular/core';
declare var $: any;

@Component({
  selector: 'app-nofound',
  templateUrl: './nofound.component.html',
  styleUrls: ['./nofound.component.css']
})
export class NofoundComponent implements OnInit {

  username;
  loggedUser;
  page = 8;
  email;
  phone;
  connected = false;
  constructor() {


  }

  ngOnInit(): void {
    
  }

}
