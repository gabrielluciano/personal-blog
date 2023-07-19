import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { Store } from '@ngrx/store';
import { AppState } from '../state/app.state';
import { selectAuthIsEditor } from '../state/auth/auth.selectors';

export const editorGuard: () => Observable<boolean> = () => {
  const store = inject(Store<AppState>);
  const router = inject(Router);

  return store
    .select(selectAuthIsEditor)
    .pipe(tap((isEditor) => isEditor || router.navigate(['/'])));
};
