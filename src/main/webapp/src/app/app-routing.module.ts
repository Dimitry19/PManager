import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { ProductsComponent } from './products/products.component';
import { ProductComponent } from './product/product.component';
import { NofoundComponent } from './nofound/nofound.component';
import { AdminGuard } from "./admin/admin.guard";
import { AddProductComponent } from './add-product/add-product.component';


const routes: Routes = [
  { path:'index', component: HeaderComponent},
  { path: '',   redirectTo: '/index', pathMatch: 'full' },
  { path:'annonces/:page', component: ProductsComponent},
  // { path: '', redirectTo: '/nofound'},

  // { path: '**', canActivate: [AdminGuard], redirectTo: 'home' },
  { path:'home', canActivate: [AdminGuard], component: HomeComponent},
  { path:'profile', canActivate: [AdminGuard], component: ProfileComponent},
  { path:'addAnnonce/:type', canActivate: [AdminGuard], component: AddProductComponent},
  { path:'annonce/:id', canActivate: [AdminGuard], component: ProductComponent},

  // { path: '', canActivate: [AdminGuard], component:  },
  { path:'**', component: NofoundComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
