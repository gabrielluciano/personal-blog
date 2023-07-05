import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogComponent } from './confirm-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

describe('ConfirmDialogComponent', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<ConfirmDialogComponent>>;

  const matDialogDataMock: { message: string } = { message: 'Some message' };

  beforeEach(() => {
    dialogRefSpy = jasmine.createSpyObj<MatDialogRef<ConfirmDialogComponent>>('MatDialogRef', [
      'close',
    ]);

    TestBed.configureTestingModule({
      declarations: [ConfirmDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: matDialogDataMock },
      ],
    });
    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call dialogRef close method with true when submit button is clicked', () => {
    const button = fixture.elementRef.nativeElement.querySelector('button[color="primary"]');
    button.click();

    expect(dialogRefSpy.close).toHaveBeenCalledWith(true);
  });

  it('should call dialogRef close method with false when cancel button is clicked', () => {
    const button = fixture.elementRef.nativeElement.querySelector('button[color="warn"]');
    button.click();

    expect(dialogRefSpy.close).toHaveBeenCalledWith(false);
  });
});
