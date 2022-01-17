import { Component, OnInit, Injectable} from '@angular/core';

@Injectable()

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  connected = false;

  constructor() {

  }

  ngOnInit(): void {

  }
  
}
