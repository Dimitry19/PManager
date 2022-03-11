import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbDateParserFormatter, NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import _ from 'underscore';
import { AuthRequest } from '../auth-request';
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
  constructor(private http: Http, private router: Router,private startup: AuthRequest,public formatter: NgbDateParserFormatter,
    private calendar: NgbCalendar,private notifyService : AlertService, private route: ActivatedRoute) { 

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
        this.notifyService.showError("Une erreure est survenue","");
      }


    });
  }

  ngOnInit(): void {
  }

  detail(id){
    this.router.navigate(["/annonce",id]);
  }

  tracbByFn(index){
    return index;
  }

}
