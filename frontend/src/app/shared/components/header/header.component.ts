import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { LoginFormComponent } from '../login-form/login-form.component';
import { Store } from '@ngrx/store';
import { logout } from '../../state/auth/auth.actions';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { showSnackBar } from '../snackbar/snackbar.component';
import { SUCCESS_LOGOUT_MSG } from 'src/app/i18n/pt/msg';
import { AppState } from '../../state/app.state';
import { selectAuthIsAuthenticated } from '../../state/auth/auth.selectors';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  authenticated$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) {
    this.authenticated$ = store.select(selectAuthIsAuthenticated);
  }

  openDialog() {
    this.dialog.open(LoginFormComponent, {
      width: '100%',
      maxWidth: '560px',
      panelClass: 'dialog',
    });
  }

  logout() {
    this.authService.logout();
    this.store.dispatch(logout());
    showSnackBar(this._snackBar, SUCCESS_LOGOUT_MSG, 'success');
  }
}
