import { Component, OnInit } from '@angular/core';
import {Kitchen} from "../kitchen";
import {Observable} from "rxjs/index";
import {Currency} from "../Currency";
import {EmployeeService} from "../employee.service";
import {Router} from "@angular/router";
import {City} from "../city";

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.css']
})
export class CurrencyListComponent implements OnInit {
  // currencies: Observable<Currency[]>;
  Currency: Currency = new Currency();
  public currencies=[];
  public countryList=[];
  ButtonStatus : String="InActive";
  inactiveStatus :String ="1";
  clicked : Boolean = false;
  firstPage :Boolean = true;
  lastPage :Boolean = false;
  prevPage :Boolean = false;
  pageNo :number = 1;
  noOfPages : number;
  first : Boolean;
  last : Boolean;
  prev : Boolean;
  next : Boolean;
  page : String ='firstPage';
  constructor(private employeeService: EmployeeService,
              private router: Router) {}
  ngOnInit() {
    this.paginationList(this.page);
    this.getCountryList();
  }
  getCountryList = function () {
    this.employeeService.getAllCountryList().subscribe(data=> {
      this.countryList = data.data;
    })
  };
  paginationList(page){
    switch (page){
      case 'firstPage':
        this.pageNo = 1;
        break;
      case 'lastPage':
        this.pageNo = this.noOfPages;
        break;
      case 'nextPage':
        this.pageNo = this.pageNo + 1;
        break;
      case 'prevPage':
        this.pageNo = this.pageNo - 1;
        break;
      default:
        this.pageNo=1;
        this.firstPage = true;
    }
    this.employeeService.getPaginatedCurrencyList(this.inactiveStatus,"",(this.pageNo.toString())).subscribe(data=>
    {
      this.currencies = data.data;
      for(let cat of this.currencies) {
        if (cat.currency_status == 0) {
          cat.currency_status = "InActive";
        } else {
          cat.currency_status = "Active";
        }
      }
      this.noOfPages=data.no_of_paginations;
      if (this.noOfPages == 1||this.noOfPages==0) {
        this.first = true;
        this.last = true;
        this.prev = true;
        this.next = true;
      }else if(this.pageNo == this.noOfPages){
        this.first = false;
        this.last = true;
        this.prev = false;
        this.next = true;
      }else if(this.pageNo==1){
        this.first = true;
        this.last = false;
        this.prev = true;
        this.next = false;
      }else if(this.pageNo < this.noOfPages){
        this.first = false;
        this.last = false;
        this.prev = false;
        this.next = false;
      }
    })
  }
  inactiveButton(){
    if(this.clicked ==false) {
      this.inactiveStatus = "0";
      this.ButtonStatus = "Active";
    }
    else {
      this.inactiveStatus = "1";
      this.ButtonStatus = "InActive";
    }
    this.clicked = !this.clicked;
    this.paginationList('');
  }

  updateCurrency(data){
    this.Currency=data;
    if (data.currency_status == "Active") {
      data.currency_status = "1";
    } else {
      data.currency_status = "0";
    }
    this.openModal(true);
  }
  private mdlSampleIsOpen : boolean = false;
  private openModal(open : boolean) : void {
    this.mdlSampleIsOpen = open;
  }
  private submitted: boolean;

  onSubmit() {
    this.submitted = true;
    this.save();
    this.openModal(false);
  }
  save() {
    this.employeeService.createCurrency(this.Currency)
      .subscribe(data => console.log(data), error => console.log(error));
    this.Currency = new Currency();
    this.paginationList(this.page);

  }
}
