import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { SnackbarComponent, SnackbarData } from './snackbar.component';

describe('SnackbarComponent', () => {
  let component: SnackbarComponent;
  let fixture: ComponentFixture<SnackbarComponent>;
  let snackBarRefSpy: jasmine.SpyObj<MatSnackBarRef<SnackbarComponent>>;

  const snackbarDataMock: SnackbarData = {
    message: 'Some message',
    style: 'error',
  };

  beforeEach(async () => {
    snackBarRefSpy = jasmine.createSpyObj<MatSnackBarRef<SnackbarComponent>>('MatSnackBarRef', [
      'dismissWithAction',
    ]);

    await TestBed.configureTestingModule({
      imports: [SnackbarComponent],
      providers: [
        { provide: MAT_SNACK_BAR_DATA, useValue: snackbarDataMock },
        { provide: MatSnackBarRef, useValue: snackBarRefSpy },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(SnackbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call dismissWithAction when close button is clicked', () => {
    const button = fixture.elementRef.nativeElement.querySelector('button');
    button.click();
    expect(snackBarRefSpy.dismissWithAction).toHaveBeenCalled();
  });
});
