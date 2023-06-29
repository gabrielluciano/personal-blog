import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { Observable, catchError } from 'rxjs';
import { handleError } from '../util/errorHandling';
import { PostCreateRequest } from 'src/app/models/post/postCreateRequest';

@Injectable({
  providedIn: 'root',
})
export class PostsService {
  readonly API = 'http://localhost:8080/';
  readonly DEFAULT_PAGE_SIZE = 10;
  readonly DEFAULT_PAGE_INDEX = 0;

  constructor(private http: HttpClient) { }

  list(pageSize: number, pageIndex: number, tagId?: number): Observable<Page<PostReponse>> {
    if (!pageSize && pageSize != 0) {
      pageSize = this.DEFAULT_PAGE_SIZE;
    }

    if (!pageIndex && pageIndex != 0) {
      pageIndex = this.DEFAULT_PAGE_INDEX;
    }

    return this.http
      .get<Page<PostReponse>>(this.API + 'posts', {
        params: new HttpParams()
          .set('tag', tagId || '')
          .set('sort', 'createdAt,desc')
          .set('size', pageSize)
          .set('page', pageIndex),
      })
      .pipe(catchError(handleError));
  }

  findBySlug(slug: string): Observable<PostReponse> {
    return this.http
      .get<PostReponse>(this.API + 'posts/slug/' + slug)
      .pipe(catchError(handleError));
  }

  save(post: PostCreateRequest): Observable<PostReponse> {
    return this.http
      .post<PostReponse>(this.API + 'posts', post)
      .pipe(catchError(handleError));
  }

  addTag(postId: number, tagId: number): Observable<void> {
    return this.http.put<void>(`${this.API}posts/${postId}/tags/${tagId}`, null);
  }
}
