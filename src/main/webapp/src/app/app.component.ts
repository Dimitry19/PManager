import { Component} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {


  constructor(private translate: TranslateService) {
    let self = this;
    // self.translate.addLangs(["fr","it","en"]);
    self.translate.setDefaultLang('fr');
    // self.translate.use('it');
  }

}
