import { createAction, props } from '@ngrx/store';
import { JwtToken } from 'src/app/models/jwtToken';

export const authenticate = createAction(
  '[App Component] Authenticate',
  props<{ token: JwtToken }>(),
);

export const login = createAction('[LoginForm Component] Login', props<{ token: JwtToken }>());

export const logout = createAction('[Header Component] Logout');
