import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TablePosListComponent } from './table-pos-list.component';

describe('TablePosListComponent', () => {
  let component: TablePosListComponent;
  let fixture: ComponentFixture<TablePosListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TablePosListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TablePosListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
