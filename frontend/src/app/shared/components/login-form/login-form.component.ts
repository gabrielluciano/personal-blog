import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatDialogRef } from '@angular/material/dialog';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { SUCCESS_LOGIN_MSG } from 'src/app/i18n/pt/msg';
import { AuthService } from '../../services/auth.service';
import { StorageService } from '../../services/storage.service';
import { AppState } from '../../state/app.state';
import { login } from '../../state/auth/auth.actions';
import { HeaderComponent } from '../header/header.component';
import { showSnackBar } from '../snackbar/snackbar.component';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormField, MatLabel, MatInput, MatError, MatButton],
})
export class LoginFormComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private store: Store<AppState>,
    private _snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<HeaderComponent>,
    private storageService: StorageService,
  ) {}

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
      next: this.handleSucessfulLogin.bind(this),
      error: this.handleLoginError.bind(this),
    });
  }

  private async handleSucessfulLogin(token: string) {
    this.storageService.setItem('access_token', token);
    const decodedToken = await this.authService.getDecodedToken(token);
    if (decodedToken) {
      this.store.dispatch(login({ token: decodedToken }));
    }
    showSnackBar(this._snackBar, SUCCESS_LOGIN_MSG, 'success');
    this.dialogRef.close();
  }

  private async handleLoginError(error: HttpErrorResponse) {
    showSnackBar(this._snackBar, error.message, 'error');
  }
}
