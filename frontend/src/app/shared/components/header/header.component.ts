import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginFormComponent } from '../login-form/login-form.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  constructor(public dialog: MatDialog) {}

  openDialog() {
    this.dialog.open(LoginFormComponent, {
      width: '100%',
      maxWidth: '560px',
      panelClass: 'dialog',
    });
  }
}
