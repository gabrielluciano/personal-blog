import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard = async () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (await authService.isEditor()) {
    return true;
  }
  router.navigate(['/']);
  return false;
};
