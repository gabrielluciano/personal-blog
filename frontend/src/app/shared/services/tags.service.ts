import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, catchError, throwError } from 'rxjs';
import { ErrorDetails } from 'src/app/models/errorDetails';
import { TagResponse } from 'src/app/models/tag/tagResponse';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  readonly API = 'http://localhost:8080/';

  constructor(private http: HttpClient, private router: Router) {}

  findById(id: number): Observable<TagResponse> {
    return this.http.get<TagResponse>(this.API + 'tags/' + id).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    const errorDetails: Partial<ErrorDetails> = error.error;
    return throwError(() => errorDetails);
  }
}
