import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable, catchError } from 'rxjs';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import { handleError } from '../util/errorHandling';
import { Page } from 'src/app/models/page';
import { environment as env } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  constructor(private http: HttpClient) {}

  list(): Observable<Page<TagResponse>> {
    return this.http.get<Page<TagResponse>>(env.apiUrl + 'tags?size=100');
  }

  findById(id: number): Observable<TagResponse> {
    return this.http.get<TagResponse>(env.apiUrl + 'tags/' + id).pipe(catchError(handleError));
  }
}
