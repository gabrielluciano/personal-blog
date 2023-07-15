import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, catchError } from 'rxjs';
import { handleError } from '../util/errorHandling';
import { JwtToken } from 'src/app/models/jwtToken';
import { environment as env } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {}

  login(email: string, password: string): Observable<string> {
    return this.http
      .post(
        env.apiUrl + 'login',
        { email, password },
        {
          responseType: 'text',
        }
      )
      .pipe(catchError(handleError));
  }

  logout() {
    localStorage.removeItem('access_token');
  }

  async isAuthenticated() {
    try {
      if (await this.jwtHelper.isTokenExpired()) {
        this.logout();
        return false;
      }
      return true;
    } catch (error) {
      return false;
    }
  }

  async isEditor() {
    const isAuthenticated = await this.isAuthenticated();

    if (isAuthenticated) {
      const token = await this.jwtHelper.decodeToken<JwtToken>();
      return token ? token.roles.includes('EDITOR') : false;
    }
    return false;
  }
}
