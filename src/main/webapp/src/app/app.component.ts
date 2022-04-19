import { Component} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
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
      $('[data-toggle="tooltip"]').tooltip()
  });
    // private translate: TranslateService
    // // self.translate.addLangs(["fr","it","en"]);
    // self.translate.setDefaultLang('fr');
    // // self.translate.use('it');
    
  }

}
