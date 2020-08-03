import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private baseUrl = 'http://localhost:8096/hipos';

  constructor(private http: HttpClient) {
    document.cookie = "restaurantId=" + "lKwWsJ";
    document.cookie = "userName=" + "admin";
    document.cookie = "connect=" + "lKwWsJ213";
  }

  getEmployee(id: number): Observable<any> {

    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createEmployee(employee: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveEmployee`, employee);
  }
  createDelivery(delivery: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveDeliveryArea`, delivery);
  }

 createCurrency(Currency: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveCurrency`, Currency);
  }
  createTableZone(tablezone: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/savetableconfiguration`, tablezone);
  }
  createPaymentMethod(paymentmethod: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/savePaymentmethod`, paymentmethod);
  }

  updateEmployee(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }
  updateCurrency(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }
  updateDelivery(id: number, value: any): Observable<Object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteEmployee(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  deleteTableZone(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  deletePaymentMethod(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  deleteDelivery(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  deleteCurrency(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getDeliveryAreaList(inactiveStatus : String, shiftSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedDeliveryAreaList?&type=` + inactiveStatus+ `&searchText=` + shiftSearchText + `&pageNo=`+ pageNo,"");
  }

  getTableZoneList(inactiveStatus : String, tableConfigSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedTableconfiList?&type=` + inactiveStatus+ `&searchText=` + tableConfigSearchText + `&pageNo=`+ pageNo,"");
  }
  getEmployeesList(inactiveStatus : String, employeeSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedEmployeeList?&type=` + inactiveStatus+ `&searchText=` + employeeSearchText + `&pageNo=`+ pageNo,"");
  }
  getPaginatedCurrencyList(inactiveStatus : String, searchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedCurrencyList?&type=` + inactiveStatus+ `&searchText=` + searchText + `&pageNo=`+ pageNo,"");
  }
  getAgentList(inactiveStatus : String, agentSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedAgentList?&type=` + inactiveStatus+ `&searchText=` + agentSearchText + `&pageNo=`+ pageNo,"");
  }
  getDiscountMethodList(inactiveStatus : String, agentSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedDiscountList?&type=` + inactiveStatus+ `&searchText=` + agentSearchText + `&pageNo=`+ pageNo,"");
  }
  getCountry(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createCountry(country: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveCountry`, country);
  }

  getCountryList(inactiveStatus : String,countrySearchText:String,pageNo:String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getpaginatedcountry?type=`+ inactiveStatus+`&searchText=`+countrySearchText+`&pageNo=`+ pageNo,"");
  }

  createKitchen(kitchen: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveKitchen`, kitchen);
  }

  getPaymentMethodList(inactiveStatus : String, paymentSearchText :String, pageNo:String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedPaymentMethodList?&type=` + inactiveStatus+ `&searchText=` + paymentSearchText + `&pageNo=`+ pageNo,"");
  }

  getKitchenList(inactiveStatus : String,kitchenSearchText:String,pageNo:String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getpaginatedKitchen?type=`+ inactiveStatus+`&searchText=`+kitchenSearchText+`&pageNo=`+ pageNo,"");
  }
  createCustomer(customer: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveCustomer`, customer);
  }

  createStaffGroup(customer: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveStaffGroup`, customer);
  }

  getStaffGroupList(inactiveStatus : String, customerSearchText:String, pageNo:String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginationStaffGroupList?type=`+ inactiveStatus+`&searchText=`+customerSearchText+`&pageNo=`+ pageNo,"");
  }
  getCustomerList(inactiveStatus : String, customerSearchText:String, pageNo:String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginationCustomerList?type=`+ inactiveStatus+`&searchText=`+customerSearchText+`&pageNo=`+ pageNo,"");
  }

  createShift(shift: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveShift`, shift);
  }
  getShiftList(inactiveStatus : String, shiftSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedShiftList?&type=` + inactiveStatus+ `&searchText=` + shiftSearchText + `&pageNo=`+ pageNo,"");
  }
  createCategory(category: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveNewItemCategory`, category);
  }

  getPaginatedCategoryList(inactiveStatus : String, shiftSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedCategoryList?&type=` + inactiveStatus+ `&searchText=` + shiftSearchText + `&pageNo=`+ pageNo,"");
  }
  deleteCategory(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  createAgent(Agent: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveNewAgent`, Agent);
  }


  deleteAgent(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  createDiscount(Discount: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveDiscountmethod`, Discount);
  }

  createItem(Item: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveNewItem`, Item);
  }


  deleteItem(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
  createTable(shift: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveTable`, shift);
  }
  createState(state: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveState`, state);
  }
  getStateList(inactiveStatus : String, searchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedState?&type=` + inactiveStatus+ `&searchText=` + searchText + `&pageNo=`+ pageNo,"");
  }
  getTableList(inactiveStatus : String, searchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getpaginatedtable?&type=` + inactiveStatus+ `&searchText=` + searchText + `&pageNo=`+ pageNo,"");
  }
  getItemsList(inactiveStatus : String, itemSearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedItemList?&type=` + inactiveStatus+ `&searchText=` + itemSearchText + `&pageNo=`+ pageNo,"");
  }

  createCity(state: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveCity`, state);
  }
  getCityList(inactiveStatus : String, citySearchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedCityList?&type=` + inactiveStatus+ `&searchText=` + citySearchText + `&pageNo=`+ pageNo,"");
  }

  createPaymentVoucher(paymentVoucher: Object): Observable<Object> {
    return this.http.post(`${this.baseUrl}/saveVoucher`, paymentVoucher);
  }

  getPaymentVoucherList(inactiveStatus : String, searchText :String, pageNo : String): Observable<any> {
    return this.http.post(`${this.baseUrl}/getPaginatedVoucherList?&type=` + inactiveStatus+ `&searchText=` + searchText + `&pageNo=`+ pageNo,"");
  }

  getAllCountryList(){
    return this.http.post(`${this.baseUrl}/getCountryList`,'');

  }
  getAllStateList(){
    return this.http.post(`${this.baseUrl}/getAllStateList`,'');

  }

  getAllStaffList(){
    return this.http.post(`${this.baseUrl}/getAllStaffList`,'');

  }

  getAllCityList(){
    return this.http.post(`${this.baseUrl}/getAllCityList`,'');

  }

  getAllZoneList(){
    return this.http.post(`${this.baseUrl}/getAllZoneList`,'');

  }
  getAllCategoryList(){
    return this.http.post(`${this.baseUrl}/getAllCategoryList`,'');

  }
  getAllShiftList(){
    return this.http.post(`${this.baseUrl}/getAllShiftList`,'');

  }
}
