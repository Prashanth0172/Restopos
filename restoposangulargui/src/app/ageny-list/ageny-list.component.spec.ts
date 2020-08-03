import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgenyListComponent } from './ageny-list.component';

describe('AgenyListComponent', () => {
  let component: AgenyListComponent;
  let fixture: ComponentFixture<AgenyListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgenyListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgenyListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
