import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../employee.service";
import {Router} from "@angular/router";
import {Observable} from "rxjs/index";
import {Item} from "../item";
import {City} from "../city";

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})
export class ItemListComponent implements OnInit {
  // Item: Observable<Item[]>;
  Item: Item = new Item();
  public itemss=[];
  public categoryList=[];
  public shiftList=[];
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
    this.getCategoryList();
    this.getShiftList();
  }

  getCategoryList = function () {
    this.employeeService.getAllCategoryList().subscribe(data=> {
      this.categoryList = data.data;
    })
  };
  getShiftList = function () {
    this.employeeService.getAllShiftList().subscribe(data=> {
      this.shiftList = data.data;
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
    this.employeeService.getItemsList(this.inactiveStatus,"",(this.pageNo.toString())).subscribe(data=>
    {
      this.itemss = data.data;
      for(let cat of this.itemss) {
        if (cat.menu_status == 0) {
          cat.menu_status = "InActive";
        } else {
          cat.menu_status = "Active";
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

  deleteItem(id: number) {
    this.employeeService.deleteItem(id)
      .subscribe(
        data => {
          console.log(data);

        },
        error => console.log(error));
  }


  updateItem(data){
    this.Item=data;
    if(data.taxtype=="Inclusive"){
      data.taxtype = true;
    }else {
      data.taxtype=false ;
    }
    if (data.menu_status == "Active") {
      data.menu_status = "1";
    } else {
      data.menu_status = "0";
    }
    if (data.itemType == "Veg") {
      data.itemType = "1";
    } else if(data.itemType == "NonVeg") {
      data.itemType = "2";
    }
    else if(data.itemType == "MixedEgg"){
      data.itemType = "3";
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
    this.employeeService.createItem(this.Item)
      .subscribe(data => console.log(data), error => console.log(error));
    this.Item = new Item();
    this.paginationList(this.page);
  }
}
