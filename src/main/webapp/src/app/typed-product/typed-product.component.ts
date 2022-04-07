import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbDateParserFormatter, NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
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
  constructor(private router: Router,private startup: ServiceRequest,public formatter: NgbDateParserFormatter,
    private notifyService : AlertService) { 

    this.startup.annoncesList(0).toPromise().then(response =>{
      // console.log(response);
      if(!response.retCode){
        this.list_A = _.filter(response.results, param =>{
          return param.transport === "PLANE";
        });
        this.list_B = _.filter(response.results, param =>{
          return param.transport == "NAVE";
        });
        this.list_V = _.filter(response.results, param =>{
          return param.transport == "AUTO";
        });
      }else{
        this.notifyService.showError(response.message,"");
      }


    });
  }

  ngOnInit(): void {
  }

  detail(id){
    this.router.navigate(["/annonce",id,"OTHER"]);
  }

  tracbByFn(index){
    return index;
  }

}
