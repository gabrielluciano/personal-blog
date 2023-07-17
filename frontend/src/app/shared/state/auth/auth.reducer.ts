import { createReducer, on } from '@ngrx/store';
import { authenticate, login, logout } from './auth.actions';

export interface AuthState {
  isAuthenticated: boolean;
  isEditor: boolean;
  isAdmin: boolean;
}

export const initialState: AuthState = {
  isAuthenticated: false,
  isEditor: false,
  isAdmin: false,
};

export const authReducer = createReducer(
  initialState,
  on(authenticate, (state, { token }) => ({
    isAuthenticated: true,
    isEditor: token.roles.includes('EDITOR'),
    isAdmin: token.roles.includes('ADMIN'),
  })),
  on(login, (state, { token }) => ({
    isAuthenticated: true,
    isEditor: token.roles.includes('EDITOR'),
    isAdmin: token.roles.includes('ADMIN'),
  })),
  on(logout, () => initialState)
);
