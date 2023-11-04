import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { AppState } from '../../state/app.state';
import { AuthService } from '../../services/auth.service';
import { selectAuthIsAuthenticated } from '../../state/auth/auth.selectors';
import { LoginFormComponent } from '../login-form/login-form.component';
import { logout } from '../../state/auth/auth.actions';
import { SUCCESS_LOGOUT_MSG } from 'src/app/i18n/pt/msg';
import { showSnackBar } from '../snackbar/snackbar.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent {
  @Input() show: boolean = false;
  @Output() whenClose: EventEmitter<void> = new EventEmitter();
  authenticated$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar,
  ) {
    this.authenticated$ = store.select(selectAuthIsAuthenticated);
  }

  closeMenu() {
    this.whenClose.emit();
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
