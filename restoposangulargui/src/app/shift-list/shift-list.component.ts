import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs/index";
import {Router} from "@angular/router";
import {Shift} from "../shift";
import {EmployeeService} from "../employee.service";
import {City} from "../city";

@Component({
  selector: 'app-shift-list',
  templateUrl: './shift-list.component.html',
  styleUrls: ['./shift-list.component.css']
})
export class ShiftListComponent implements OnInit {

  employees: Observable<Shift[]>;
  shift: Shift = new Shift();
  public shifts=[];
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
    this.employeeService.getShiftList(this.inactiveStatus,"",(this.pageNo.toString())).subscribe(data=>
    {
      this.shifts = data.data;
      for(let cat of this.shifts) {
        if (cat.mealtime_status == 0) {
          cat.mealtime_status = "InActive";
        } else {
          cat.mealtime_status = "Active";
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
  updateEmployee(id: number){
    this.router.navigate(['update', id]);
  }

  updateShift(data){
    this.shift=data;
    if (data.mealtime_status == "Active") {
      data.mealtime_status = "1";
    } else {
      data.mealtime_status = "0";
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
    this.employeeService.createShift(this.shift)
      .subscribe(data => console.log(data), error => console.log(error));
    this.shift = new Shift();
    this.paginationList(this.page);

  }
}
