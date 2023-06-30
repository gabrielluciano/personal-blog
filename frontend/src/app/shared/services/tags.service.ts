import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, catchError } from 'rxjs';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import { handleError } from '../util/errorHandling';
import { Page } from 'src/app/models/page';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  readonly API = 'http://localhost:8080/';

  constructor(private http: HttpClient, private router: Router) {}

  list(): Observable<Page<TagResponse>> {
    return this.http.get<Page<TagResponse>>(this.API + 'tags?size=100');
  }

  findById(id: number): Observable<TagResponse> {
    return this.http.get<TagResponse>(this.API + 'tags/' + id).pipe(catchError(handleError));
  }
}
