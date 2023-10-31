import { Component, OnInit } from '@angular/core';
import { AuthService } from './shared/services/auth.service';
import { Store } from '@ngrx/store';
import { authenticate } from './shared/state/auth/auth.actions';
import { AppState } from './shared/state/app.state';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  constructor(
    private store: Store<AppState>,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.setAuthentication();
  }

  private async setAuthentication() {
    const decodedToken = await this.authService.getDecodedToken();
    if (decodedToken) {
      this.store.dispatch(authenticate({ token: decodedToken }));
    }
  }
}
