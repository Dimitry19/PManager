import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HeaderComponent} from './header/header.component';
import {HomeComponent} from './home/home.component';
import {ProfileComponent} from './profile/profile.component';
import {ProductsComponent} from './products/products.component';
import {ProductComponent} from './product/product.component';
import {NofoundComponent} from './nofound/nofound.component';
import {AdminGuard} from "./admin/admin.guard";
import {AddProductComponent} from './add-product/add-product.component';


const routes: Routes = [
  { path:'index', component: HeaderComponent, data: { breadcrumb: 'Index' }},
  { path: '',   redirectTo: '/index', pathMatch: 'full' },
  { path:'annonces/:page', component: ProductsComponent, data: { breadcrumb: 'Annonces' }},
  // { path: '', redirectTo: '/nofound'},

  // { path: '**', canActivate: [AdminGuard], redirectTo: 'home' },
  { path:'home', canActivate: [AdminGuard], component: HomeComponent, data: { breadcrumb: 'Home' }},
  { path:'myaccount', canActivate: [AdminGuard], component: ProfileComponent, data: { breadcrumb: 'Mon compte' }},
  { path:'addAnnonce/:type', canActivate: [AdminGuard], component: AddProductComponent, data: { breadcrumb: 'Ajouter une annonce' }},
  { path:'annonce/:id/:notification', canActivate: [AdminGuard], component: ProductComponent, data: {
    // breadcrumb: (resolvedId: string) => ` ${resolvedId} `
    breadcrumb: 'annonce'
  },
  children: [
    {
      path: ':read',
      canActivate: [AdminGuard],
      component: ProductComponent,
      data: {
        breadcrumb: 'read'
      }
    },{
      path: ':modify',
      canActivate: [AdminGuard],
      component: ProductComponent,
      data: {
        breadcrumb: 'modify'
        // label: 'read',
        // info: { myData: { icon: 'home', iconType: 'material' } }
      }
    }
  ]
  },
  { path:'profile/:id', canActivate: [AdminGuard], component:  ProfileComponent, data: { breadcrumb: 'Profile' }},
  { path:'**', component: NofoundComponent, data: { breadcrumb: 'not found' }}


];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
