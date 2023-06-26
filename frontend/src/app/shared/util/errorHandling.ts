import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

import { ErrorDetails } from 'src/app/models/errorDetails';

export function handleError(error: HttpErrorResponse) {
  const errorDetails: Partial<ErrorDetails> = JSON.parse(error.error);
  return throwError(() => errorDetails);
}
