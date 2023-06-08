import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomDateComponent } from './custom-date.component';

describe('CustomDateComponent', () => {
  let component: CustomDateComponent;
  let fixture: ComponentFixture<CustomDateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomDateComponent]
    });
    fixture = TestBed.createComponent(CustomDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
