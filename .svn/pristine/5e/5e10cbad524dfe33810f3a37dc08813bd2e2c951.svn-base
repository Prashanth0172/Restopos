import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs/index";
import {Router} from "@angular/router";
import {Shift} from "../shift";
import {EmployeeService} from "../employee.service";
import {StaffGroup} from "../staffGroup";

@Component({
  selector: 'app-staff-group-list',
  templateUrl: './staff-group-list.component.html',
  styleUrls: ['./staff-group-list.component.css']
})
export class StaffGroupListComponent implements OnInit {

  employees: Observable<StaffGroup[]>;
  staffGroups: StaffGroup = new StaffGroup();
  public staffGroupss=[];
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
    this.employeeService.getStaffGroupList(this.inactiveStatus,"",(this.pageNo.toString())).subscribe(data=>
    {
      this.staffGroupss = data.data;
      for(let cat of this.staffGroupss) {
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

  updateStaffGroup(data){
    this.staffGroups=data;
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
    this.employeeService.createStaffGroup(this.staffGroups)
      .subscribe(data => console.log(data), error => console.log(error));
    this.staffGroups = new StaffGroup();
    this.paginationList(this.page);

  }

}
