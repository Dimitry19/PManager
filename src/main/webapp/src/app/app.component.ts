import {Component} from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {


  constructor() {
    let self = this;
    $(document).ready(function () {
      $('[rel="tooltip"]').tooltip()
  });
    // private translate: TranslateService
    // // self.translate.addLangs(["fr","it","en"]);
    // self.translate.setDefaultLang('fr');
    // // self.translate.use('it');
    
  }

}
