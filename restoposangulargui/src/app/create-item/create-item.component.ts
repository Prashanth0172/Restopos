import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../employee.service";
import {Router} from "@angular/router";
import {Item} from "../item";

@Component({
  selector: 'app-create-item',
  templateUrl: './create-item.component.html',
  styleUrls: ['./create-item.component.css']
})
export class CreateItemComponent implements OnInit {
  Item: Item = new Item();
  submitted = false;

  constructor(private employeeService: EmployeeService,
              private router: Router) { }

  ngOnInit() {
  }
  newItem(): void {
    this.submitted = false;
    this.Item = new Item();
  }
  save() {
    this.employeeService.createItem(this.Item)
      .subscribe(data => console.log(data), error => console.log(error));
    this.Item = new Item();
    this.gotoList();
  }
  onSubmit() {
    this.submitted = true;
    this.save();
  }

  gotoList() {
    this.router.navigate(['/item']);
  }
}
