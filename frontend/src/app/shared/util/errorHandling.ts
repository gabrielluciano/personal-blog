import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { throwError } from 'rxjs';
import { UNAUTHORIZED_ERROR_MSG } from 'src/app/i18n/pt/msg';

import { ErrorDetails } from 'src/app/models/errorDetails';

export function handleError(error: HttpErrorResponse) {
  const errorType = typeof error.error;

  if (errorType === 'object' && error.error != null) {
    return throwError(() => error.error);
  } else if (errorType === 'string') {
    return throwError(() => tryToParseStringAsObject(error));
  } else {
    return throwError(() => createErrorDetails(error));
  }
}

function tryToParseStringAsObject(error: HttpErrorResponse) {
  let errorObject: Partial<ErrorDetails>;
  try {
    errorObject = JSON.parse(error.error);
  } catch (_) {
    errorObject = createErrorDetails(error);
  }
  return errorObject;
}

function createErrorDetails(error: HttpErrorResponse) {
  const errorDetails: Partial<ErrorDetails> = {
    status: error.status,
    path: '' + error.url,
  };

  if (error.status == HttpStatusCode.Unauthorized) {
    errorDetails.message = UNAUTHORIZED_ERROR_MSG;
  } else {
    errorDetails.message = error.message;
  }

  return errorDetails;
}
