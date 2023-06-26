import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  readonly API = 'http://localhost:8080/';

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {}

  login(email: string, password: string): Observable<string> {
    return this.http.post(
      this.API + 'login',
      { email, password },
      {
        responseType: 'text',
      }
    );
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
}
