import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JwtHelperService } from '@auth0/angular-jwt';
import { JwtToken } from 'src/app/models/jwtToken';

describe('AuthService', () => {
  const JWT_VALIDATION_REGEX = /^(ey([a-zA-Z0-9-_])*.){2}[a-zA-Z0-9-_]*/g;
  const JWT_TOKEN =
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

  let service: AuthService;
  let jwtHelper: jasmine.SpyObj<JwtHelperService>;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    jwtHelper = jasmine.createSpyObj<JwtHelperService>('JwtHelperService', [
      'isTokenExpired',
      'decodeToken',
    ]);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: JwtHelperService, useValue: jwtHelper }],
    });
    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return an jwt token when email and password are correct', () => {
    service.login('email', 'password').subscribe((token) => {
      expect(token).toBeTruthy();
      expect(token).toMatch(JWT_VALIDATION_REGEX);

      const req = httpTestingController.expectOne('http://localhost:8080/login');
      expect(req.request.method).toEqual('POST');
      req.flush(JWT_TOKEN);
    });
  });

  it('should remove access token from localStorage', () => {
    localStorage.setItem('access_token', JWT_TOKEN);
    service.logout();
    expect(localStorage.getItem('access_token')).toBeNull();
  });

  it('should return false when isAuthenticated is called and access_token doesnt exist', async () => {
    jwtHelper.isTokenExpired.and.throwError("token doesn't not exist");
    const returnValue = await service.isAuthenticated();
    expect(jwtHelper.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBeFalsy();
  });

  it('should return false when isAuthenticated is called and access_token is expired', async () => {
    jwtHelper.isTokenExpired.and.returnValue(Promise.resolve(true));
    const returnValue = await service.isAuthenticated();
    expect(jwtHelper.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBeFalsy();
  });

  it('should return true when isAuthenticated is called and access_token is valid', async () => {
    jwtHelper.isTokenExpired.and.returnValue(Promise.resolve(false));
    const returnValue = await service.isAuthenticated();
    expect(jwtHelper.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBeTruthy();
  });

  const isEditorTest = (roles: JwtToken['roles'], expected: boolean) => {
    return async () => {
      jwtHelper.isTokenExpired.and.returnValue(Promise.resolve(false));
      jwtHelper.decodeToken.and.returnValue({
        id: 1,
        email: 'email@email.com',
        iat: Date.now() / 1000,
        exp: Date.now() / 1000 + 600,
        roles: roles,
      });

      const returnValue = await service.isEditor();
      expect(jwtHelper.isTokenExpired).toHaveBeenCalled();
      expect(jwtHelper.decodeToken).toHaveBeenCalled();
      expect(returnValue).toBe(expected);
    };
  };

  it(
    'should return true when isEditor is called and user is an editor',
    isEditorTest(['EDITOR', 'ADMIN', 'USER'], true)
  );
  it(
    'should return false when isEditor is called and user is not an editor',
    isEditorTest(['USER'], false)
  );
});
