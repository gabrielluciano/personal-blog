import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule } from '@angular/material/icon';

import { SnackbarComponent, SnackbarData } from './snackbar.component';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

describe('SnackbarComponent', () => {
  let component: SnackbarComponent;
  let fixture: ComponentFixture<SnackbarComponent>;
  let snackBarRefSpy: jasmine.SpyObj<MatSnackBarRef<SnackbarComponent>>;

  const snackbarDataMock: SnackbarData = {
    message: 'Some message',
    style: 'error',
  };

  beforeEach(() => {
    snackBarRefSpy = jasmine.createSpyObj<MatSnackBarRef<SnackbarComponent>>('MatSnackBarRef', [
      'dismissWithAction',
    ]);

    TestBed.configureTestingModule({
      imports: [MatIconModule],
      declarations: [SnackbarComponent],
      providers: [
        { provide: MAT_SNACK_BAR_DATA, useValue: snackbarDataMock },
        { provide: MatSnackBarRef, useValue: snackBarRefSpy },
      ],
    });
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
