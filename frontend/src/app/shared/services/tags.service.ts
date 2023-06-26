import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, catchError } from 'rxjs';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import { handleError } from '../util/errorHandling';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  readonly API = 'http://localhost:8080/';

  constructor(private http: HttpClient, private router: Router) {}

  findById(id: number): Observable<TagResponse> {
    return this.http.get<TagResponse>(this.API + 'tags/' + id).pipe(catchError(handleError));
  }
}
