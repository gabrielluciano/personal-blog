import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './shared/shared.module';
import { JwtModule } from '@auth0/angular-jwt';
import { environment as env } from 'src/environments/environment';
import { StoreModule } from '@ngrx/store';
import { authReducer } from './shared/state/auth/auth.reducer';

export function tokenGetter() {
  if (typeof localStorage == 'object') {
    return localStorage.getItem('access_token');
  }
  return null;
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    SharedModule,
    JwtModule.forRoot({
      config: {
        tokenGetter,
        allowedDomains: env.angularJwtAllowedDomains,
      },
    }),
    StoreModule.forRoot({ auth: authReducer }),
  ],
  providers: [provideClientHydration()],
  bootstrap: [AppComponent],
})
export class AppModule {}
