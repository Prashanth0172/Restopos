import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {EmployeeService} from "./employee.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  constructor(private employeeService: EmployeeService,
              private router: Router) { }
  onSubmit() {
    this.router.navigate(['/menu'])
  }
  ngOnInit() {
  }
}
