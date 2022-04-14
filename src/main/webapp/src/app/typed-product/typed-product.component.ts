import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import _ from 'underscore';
import { ServiceRequest } from '../serviceRequest';
import { AlertService } from '../alert.service';

@Component({
  selector: 'app-typed-product',
  templateUrl: './typed-product.component.html',
  styleUrls: ['./typed-product.component.css']
})
export class TypedProductComponent implements OnInit {

  list_V = [];
  list_B = [];
  list_A = [];
  @Input() announce;
  constructor(private router: Router,private startup: ServiceRequest,public formatter: NgbDateParserFormatter,
    private notifyService : AlertService) { 

      this.startup.annoncesList(0,"HOME").toPromise().then(response =>{
        if(response.retCode != -1){
          this.announceClasses(response);
        }else{
          this.notifyService.showError(response.message,"");
        }
  
      });
   
  }

  ngOnInit(): void {
  }
  ngOnChanges(changes): void {
    // changes represente tous les inputs present sur le composant.
    // pour y acceder il faut faire : changes.nomInput et il est possibile d'avoir la valeur precedente, courante et un boolean
    // qui nous dit si c'est la premier modification ou non 
    
    if(changes.announce.currentValue){
      this.announceClasses(changes.announce.currentValue);
    }
    
  }

  detail(id){
    
    this.router.navigate(["/annonce",id,"OTHER","read"]);
  }

  announceClasses(response){
    this.list_A = _.filter(response.results, param =>{
      return param.transport === "PLANE";
    });
    this.list_B = _.filter(response.results, param =>{
      return param.transport == "NAVE";
    });
    this.list_V = _.filter(response.results, param =>{
      return param.transport == "AUTO";
    });
  }
  tracbByFn(index){
    return index;
  }


}
