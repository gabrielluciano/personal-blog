import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { throwError } from 'rxjs';

import { ErrorDetails } from 'src/app/models/errorDetails';

const UNAUTHORIZED_ERROR_MESSAGE = 'Invalid credentials to perform this operation';

export function handleError(error: HttpErrorResponse) {
  let errorDetails: Partial<ErrorDetails>;

  try {
    errorDetails = JSON.parse(error.error.toString());
  } catch (_) {
    errorDetails = createErrorDetails(error);
  }

  return throwError(() => errorDetails);
}

function createErrorDetails(error: HttpErrorResponse) {
  const errorDetails: Partial<ErrorDetails> = {
    status: error.status,
    path: '' + error.url,
  };

  if (error.status == HttpStatusCode.Unauthorized) {
    errorDetails.message = UNAUTHORIZED_ERROR_MESSAGE;
  } else {
    errorDetails.message = error.message;
  }

  return errorDetails;
}
