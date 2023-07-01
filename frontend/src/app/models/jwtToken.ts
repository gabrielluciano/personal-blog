export interface JwtToken {
  id: number;
  email: string;
  iat: number;
  exp: number;
  roles: Array<'ADMIN' | 'EDITOR' | 'USER'>;
}
