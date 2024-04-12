import { ComponentFixture, TestBed } from '@angular/core/testing';

import { provideRouter } from '@angular/router';
import { routes } from 'src/app/app.routes';
import { PillComponent } from './pill.component';

describe('PillComponent', () => {
  let component: PillComponent;
  let fixture: ComponentFixture<PillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PillComponent],
      providers: [provideRouter(routes)],
    }).compileComponents();
    fixture = TestBed.createComponent(PillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
