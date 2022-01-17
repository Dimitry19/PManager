import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { NgxSpinnerModule } from "ngx-spinner";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MultiSelectAllModule } from '@syncfusion/ej2-angular-dropdowns';
import { DataTablesModule } from 'angular-datatables';
// import { NotificationsService, SimpleNotificationsModule } from 'angular2-notifications';
// import { MyDatePickerModule } from 'mydatepicker';
import { DateTimePickerModule } from '@syncfusion/ej2-angular-calendars';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { Ng2SmartTableModule, LocalDataSource } from 'ng2-smart-table';
import { BsModalModule } from 'ng2-bs3-modal';
import { ChartsModule } from 'ng2-charts';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
// import { SocialLoginModule, AuthServiceConfig, AuthService } from "angularx-social-login";
// import { GoogleLoginProvider, FacebookLoginProvider } from "angularx-social-login";
import { AuthRequest } from './auth-request';
import { AdminGuard } from './admin/admin.guard';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { LoaderComponent } from './loader/loader.component';
import { ProfileComponent } from './profile/profile.component';
import { ProductsComponent } from './products/products.component';
import { ProductComponent } from './product/product.component';
import { NofoundComponent } from './nofound/nofound.component';
import { HeadComponent } from './head/head.component';
import { FooterComponent } from './footer/footer.component';
import { ToastrModule } from 'ngx-toastr';
import { AlertService } from './alert.service';
import { AddProductComponent } from './add-product/add-product.component';
import { SearchAllComponent } from './search-all/search-all.component';
import { TypedProductComponent } from './typed-product/typed-product.component';
import { TooltipDirective } from './directive/tooltip.directive';
import {OptionsComponent} from './options/options.component';


// import {MaterialModule} from './material.module';
// import { NgxPaginationModule } from 'ngx-pagination';

// const facebook_oauth_client_id: string = '2613006052293888';
// const google_oauth_client_id: string = '122355480042-2bd2i9bq38gsgfq7qfr50l0le4oorasu.apps.googleusercontent.com';
//
// let config = new AuthServiceConfig([
//   {
//     id: GoogleLoginProvider.PROVIDER_ID,
//     provider: new GoogleLoginProvider(google_oauth_client_id)
//   },
//   {
//     id: FacebookLoginProvider.PROVIDER_ID,
//     provider: new FacebookLoginProvider(facebook_oauth_client_id)
//   }
// ]);


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    LoaderComponent,
    ProfileComponent,
    ProductsComponent,
    ProductComponent,
    NofoundComponent,
    HeadComponent,
    FooterComponent,
    AddProductComponent,
    SearchAllComponent,
    TypedProductComponent,
    TooltipDirective,
    OptionsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    // MyDatePickerModule,
    DateTimePickerModule,
    NgSelectModule,
    Ng2SmartTableModule,
    BsModalModule,
    ChartsModule,
    NgxSpinnerModule,
    HttpModule,
    NgbModule,
    HttpClientModule,
    MultiSelectAllModule,
    DataTablesModule,
    // MaterialModule,
    // NgxPaginationModule,
    // SocialLoginModule.initialize(config),
    // SimpleNotificationsModule.forRoot(),
    AutocompleteLibModule,
    ToastrModule.forRoot(),
    AppRoutingModule
  ],

  providers: [AuthRequest, 
              AdminGuard,
              AlertService
              //{provide : LocationStrategy , useClass: HashLocationStrategy} ceci transforme http://localhost:4200/ en http://localhost:4200/#/
            ],
  bootstrap: [AppComponent]
})
export class AppModule { }
