import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';
import { FooterComponent } from './shared/components/footer/footer.component';
import { HeaderComponent } from './shared/components/header/header.component';
import { AuthService } from './shared/services/auth.service';
import { AppState } from './shared/state/app.state';
import { authenticate } from './shared/state/auth/auth.actions';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private store: Store<AppState> = inject(Store<AppState>);
  private authService: AuthService = inject(AuthService);

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
