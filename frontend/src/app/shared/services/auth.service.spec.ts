import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { JwtHelperService } from '@auth0/angular-jwt';
import { JwtToken } from 'src/app/models/jwtToken';
import { environment as env } from 'src/environments/environment';
import { StorageService } from './storage.service';

describe('AuthService', () => {
  const JWT_VALIDATION_REGEX = /^(ey([a-zA-Z0-9-_])*.){2}[a-zA-Z0-9-_]*/g;
  const JWT_TOKEN =
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

  let service: AuthService;
  let jwtHelperSpy: jasmine.SpyObj<JwtHelperService>;
  let storageServiceSpy: jasmine.SpyObj<StorageService>;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    jwtHelperSpy = jasmine.createSpyObj<JwtHelperService>('JwtHelperService', [
      'isTokenExpired',
      'decodeToken',
    ]);
    storageServiceSpy = jasmine.createSpyObj<StorageService>('StorageService', ['removeItem']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: JwtHelperService, useValue: jwtHelperSpy },
        { provide: StorageService, useValue: storageServiceSpy },
      ],
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
    });

    const req = httpTestingController.expectOne(env.apiUrl + 'login');
    expect(req.request.method).toEqual('POST');
    req.flush(JWT_TOKEN);
  });

  it('should remove access token from localStorage', () => {
    service.logout();
    expect(storageServiceSpy.removeItem).toHaveBeenCalledWith('access_token');
  });

  it('should return null when getDecodedToken is called and access_token doesnt exist', async () => {
    jwtHelperSpy.isTokenExpired.and.throwError("token doesn't not exist");
    const returnValue = await service.getDecodedToken();
    expect(jwtHelperSpy.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBeNull();
  });

  it('should return null when getDecodedToken is called and access_token is expired', async () => {
    jwtHelperSpy.isTokenExpired.and.returnValue(Promise.resolve(true));
    const returnValue = await service.getDecodedToken();
    expect(jwtHelperSpy.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBeNull();
  });

  it('should return jwtToken when getDecodedToken is called and user is authenticated', async () => {
    const token: JwtToken = {
      id: 1,
      email: 'email@email.com',
      iat: Date.now() / 1000,
      exp: Date.now() / 1000 + 600,
      roles: ['EDITOR', 'ADMIN', 'USER'],
    };

    jwtHelperSpy.isTokenExpired.and.returnValue(Promise.resolve(false));
    jwtHelperSpy.decodeToken.and.returnValue(token);

    const returnValue = await service.getDecodedToken();
    expect(jwtHelperSpy.isTokenExpired).toHaveBeenCalled();
    expect(returnValue).toBe(token);
  });
});
