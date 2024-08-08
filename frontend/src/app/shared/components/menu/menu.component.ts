import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { AsyncPipe, NgClass } from '@angular/common';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { SUCCESS_LOGOUT_MSG } from 'src/app/i18n/pt/msg';
import { AuthService } from '../../services/auth.service';
import { AppState } from '../../state/app.state';
import { logout } from '../../state/auth/auth.actions';
import { selectAuthIsAuthenticated, selectAuthIsEditor } from '../../state/auth/auth.selectors';
import { LoginFormComponent } from '../login-form/login-form.component';
import { showSnackBar } from '../snackbar/snackbar.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
  standalone: true,
  imports: [NgClass, MatIconButton, MatIcon, AsyncPipe],
})
export class MenuComponent {
  @Input() show = false;
  @Output() whenClose: EventEmitter<void> = new EventEmitter<void>();
  authenticated$: Observable<boolean>;
  editor$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar,
    private router: Router,
  ) {
    this.authenticated$ = store.select(selectAuthIsAuthenticated);
    this.editor$ = store.select(selectAuthIsEditor);
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

  newPost() {
    this.router.navigate(['/posts/new']);
    this.closeMenu();
  }
}
