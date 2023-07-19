import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { HeaderComponent } from '../header/header.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { showSnackBar } from '../snackbar/snackbar.component';
import { SUCCESS_LOGIN_MSG } from 'src/app/i18n/pt/msg';
import { Store } from '@ngrx/store';
import { login } from '../../state/auth/auth.actions';
import { HttpErrorResponse } from '@angular/common/http';
import { AppState } from '../../state/app.state';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private store: Store<AppState>,
    private _snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<HeaderComponent>
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
    localStorage.setItem('access_token', token);
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
