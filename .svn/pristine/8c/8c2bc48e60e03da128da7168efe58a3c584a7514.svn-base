import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../employee.service";
import {Router} from "@angular/router";
import {Employee} from "../employee";
import {Observable} from "rxjs/index";
import {TableZone} from "../tableZone";
import {City} from "../city";

@Component({
  selector: 'app-table-zone-list',
  templateUrl: './table-zone-list.component.html',
  styleUrls: ['./table-zone-list.component.css']
})
export class TableZoneListComponent implements OnInit {
  // tablezone: Observable<TableZone[]>;
  tablezone: TableZone = new TableZone();
  public tablezon=[];
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
  }

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
    this.employeeService.getTableZoneList(this.inactiveStatus,"",(this.pageNo.toString())).subscribe(data=>
    {
      this.tablezon = data.data;
      for(let cat of this.tablezon) {
        if (cat.status == 0) {
          cat.status = "InActive";
        } else {
          cat.status = "Active";
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

  updateTableZone(data){
    this.tablezone=data;
    if (data.status == "Active") {
      data.status = "1";
    } else {
      data.status = "0";
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
    // this.tablezone.no_of_rows=1;
    // this.tablezone.no_of_col=1;
    this.employeeService.createTableZone(this.tablezone)
      .subscribe(data => console.log(data), error => console.log(error));
    this.tablezone = new TableZone();
    this.paginationList(this.page);
  }

}
