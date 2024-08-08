export interface JwtToken {
  id: number;
  email: string;
  iat: number;
  exp: number;
  roles: ('ADMIN' | 'EDITOR' | 'USER')[];
}
