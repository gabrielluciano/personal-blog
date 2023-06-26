import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginFormComponent } from '../login-form/login-form.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  authenticated = false;

  constructor(public dialog: MatDialog, private authService: AuthService) {}

  ngOnInit(): void {
    this.setAuthenticated();
  }

  openDialog() {
    const dialogRef = this.dialog.open(LoginFormComponent, {
      width: '100%',
      maxWidth: '560px',
      panelClass: 'dialog',
    });
    dialogRef.afterClosed().subscribe(() => {
      this.setAuthenticated();
    });
  }

  async setAuthenticated() {
    this.authenticated = await this.authService.isAuthenticated();
  }

  logout() {
    this.authService.logout();
    this.setAuthenticated();
  }
}
