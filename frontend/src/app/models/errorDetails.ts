import { HttpStatusCode } from '@angular/common/http';

export interface ErrorDetails {
  title: string;
  message: string;
  status: HttpStatusCode;
  timestamp: string;
  path: string;
  fields: string;
  fieldsMessages: string;
}
