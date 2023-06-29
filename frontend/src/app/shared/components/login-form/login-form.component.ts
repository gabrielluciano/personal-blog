import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { HeaderComponent } from '../header/header.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar, } from '@angular/material/snack-bar';
import { SnackbarComponent, getSnackBarDefaultConfig } from '../snackbar/snackbar.component';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  readonly DURATION_IN_SECONDS = 10;

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<HeaderComponent>,
    private authService: AuthService,
    private _snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onClose() {
    this.dialogRef.close();
  }

  onSubmit() {
    this.authService.login(this.form.value.email, this.form.value.password).subscribe({
      next: (token) => {
        localStorage.setItem('access_token', token);
        this.dialogRef.close();
      },
      error: (error) => {
        this._snackBar.openFromComponent(SnackbarComponent, getSnackBarDefaultConfig(error.message, 'error'));
      },
    });
  }
}
