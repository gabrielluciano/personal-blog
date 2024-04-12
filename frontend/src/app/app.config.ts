import { provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter, withEnabledBlockingInitialNavigation } from '@angular/router';
import { JwtModule } from '@auth0/angular-jwt';
import { StoreModule } from '@ngrx/store';
import { environment as env } from 'src/environments/environment';
import { routes } from './app.routes';
import { authReducer } from './shared/state/auth/auth.reducer';

export function tokenGetter() {
  if (typeof localStorage == 'object') {
    return localStorage.getItem('access_token');
  }
  return null;
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withEnabledBlockingInitialNavigation()),
    provideAnimations(),
    provideClientHydration(),
    provideHttpClient(withInterceptorsFromDi(), withFetch()),
    importProvidersFrom(
      JwtModule.forRoot({
        config: {
          tokenGetter,
          allowedDomains: env.angularJwtAllowedDomains,
        },
      }),
      StoreModule.forRoot({ auth: authReducer }),
    ),
  ],
};
