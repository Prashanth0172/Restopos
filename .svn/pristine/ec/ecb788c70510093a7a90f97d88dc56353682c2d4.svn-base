import { EmployeeDetailsComponent } from './employee-details/employee-details.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { UpdateEmployeeComponent } from './update-employee/update-employee.component';
import {KitchenListComponent} from "./kitchen-list/kitchen-list.component";
import {ShiftListComponent} from "./shift-list/shift-list.component";
import {CountryListComponent} from "./country-list/country-list.component";
import {Country} from "./country";
import {TablePosListComponent} from "./table-pos-list/table-pos-list.component";
import {CustomerListComponent} from "./customer-list/customer-list.component";
import {TableZoneListComponent} from "./table-zone-list/table-zone-list.component";
import {PaymentMethodListComponent} from "./payment-method-list/payment-method-list.component";
import { DeliveryAreaListComponent } from './delivery-area-list/delivery-area-list.component';
import {Currency} from "./Currency";
import {CurrencyListComponent} from "./currency-list/currency-list.component";
import {Category} from "./Category";
import {CategoryListComponent} from "./category-list/category-list.component";
import {AgenyListComponent} from "./ageny-list/ageny-list.component";
import {DiscountListComponent} from "./discount-list/discount-list.component";
import {CreateItemComponent} from "./create-item/create-item.component";
import {ItemListComponent} from "./item-list/item-list.component";
import {StateListComponent} from "./state-list/state-list.component";
import {CityListComponent} from "./city-list/city-list.component";
import {PaymentVoucherListComponent} from "./payment-voucher-list/payment-voucher-list.component";
import {MenuComponent} from "./menu/menu.component";
import {StaffGroupListComponent} from "./staff-group-list/staff-group-list.component";

const routes: Routes = [
  // { path: '', redirectTo: 'employee', pathMatch: 'full' },
  { path: 'employees', component: EmployeeListComponent },
  { path: 'menu', component: MenuComponent },
  { path: 'update/:id', component: UpdateEmployeeComponent },
  { path: 'details/:id', component: EmployeeDetailsComponent },
  { path: 'kitchens', component: KitchenListComponent },
  { path: 'customer', component: CustomerListComponent },
  // { path: '', redirectTo: 'shift', pathMatch: 'full' },
  { path: 'shifts', component: ShiftListComponent },
  { path: 'currency', component: CurrencyListComponent },
  { path: 'countrylist', component:CountryListComponent  },
  { path: 'tablePos', component:TablePosListComponent  },
  { path: 'deliveryArea', component: DeliveryAreaListComponent },
  { path: 'country', component:CountryListComponent  },
  { path: 'details/:id', component: Country },
  { path: 'state', component:StateListComponent},
  { path: 'city', component:CityListComponent},
  { path: 'details/:id', component: Currency },
  { path: 'details/:id', component: Category },
  { path: 'categories', component: CategoryListComponent },
  { path: 'agent', component: AgenyListComponent },
  { path: 'discount', component: DiscountListComponent },
  { path: 'item/additem', component: CreateItemComponent },
  { path: 'item', component: ItemListComponent },
  // { path: '', redirectTo: 'tablezone', pathMatch: 'full' },
  { path: 'tablezone', component: TableZoneListComponent },
  // { path: '', redirectTo: 'paymentmethod', pathMatch: 'full' },
  { path: 'paymentmethod', component: PaymentMethodListComponent },
  { path: 'paymentVoucher', component: PaymentVoucherListComponent },
  { path: 'staffGroup', component: StaffGroupListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
