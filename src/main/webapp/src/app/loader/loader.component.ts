import { Component, OnInit, Input } from '@angular/core';
import { filter, map } from 'rxjs/operators';
import {Observable} from 'rxjs';
import { NgxSpinnerService } from "ngx-spinner";
import {
  Router,
  Event as RouterEvent,
  NavigationStart,
  NavigationEnd,
  NavigationCancel,
  NavigationError
} from '@angular/router';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})

export class LoaderComponent implements OnInit {

    show: boolean = true;

    constructor(private router: Router, private spinner: NgxSpinnerService) {

    }

  ngOnInit(): void {
    this.router.events.subscribe((event: RouterEvent) => {
      if (event instanceof NavigationStart) {
        this.show = true;
      }
      if (event instanceof NavigationEnd) {
        this.show = false;
      }

      // Set loading state to false in both of the below events to hide the spinner in case a request fails
      if (event instanceof NavigationCancel) {
        this.show = false;
      }
      if (event instanceof NavigationError) {
        this.show = false;
      }
    })

  }
  // Shows and hides the loading spinner during RouterEvent changes
  // navigationInterceptor(event: RouterEvent): void {
  //
  // }


}
