import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { handleError } from '../util/errorHandling';
import { PostCreateRequest } from 'src/app/models/post/postCreateRequest';
import { PostUpdateRequest } from 'src/app/models/post/postUpdateRequest';
import { environment as env } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PostsService {
  readonly DEFAULT_PAGE_SIZE = 10;
  readonly DEFAULT_PAGE_INDEX = 0;

  constructor(private http: HttpClient) {}

  list(
    pageSize: number,
    pageIndex: number,
    tagId: number | null,
    drafts = false,
  ): Observable<Page<PostReponse>> {
    if (!pageSize && pageSize != 0) {
      pageSize = this.DEFAULT_PAGE_SIZE;
    }

    if (!pageIndex && pageIndex != 0) {
      pageIndex = this.DEFAULT_PAGE_INDEX;
    }

    return this.http
      .get<Page<PostReponse>>(env.apiUrl + 'posts', {
        params: new HttpParams()
          .set('tag', tagId || '')
          .set('sort', 'createdAt,desc')
          .set('size', pageSize)
          .set('page', pageIndex)
          .set('drafts', drafts),
      })
      .pipe(catchError(handleError));
  }

  findBySlug(slug: string): Observable<PostReponse> {
    return this.http
      .get<PostReponse>(env.apiUrl + 'posts/slug/' + slug)
      .pipe(catchError(handleError));
  }

  save(post: PostCreateRequest): Observable<PostReponse> {
    return this.http.post<PostReponse>(env.apiUrl + 'posts', post).pipe(catchError(handleError));
  }

  update(post: PostUpdateRequest, id: number): Observable<void> {
    return this.http.put<void>(env.apiUrl + 'posts/' + id, post).pipe(catchError(handleError));
  }

  addTag(postId: number, tagId: number): Observable<void> {
    return this.http.put<void>(`${env.apiUrl}posts/${postId}/tags/${tagId}`, null);
  }

  removeTag(postId: number, tagId: number): Observable<void> {
    return this.http.delete<void>(`${env.apiUrl}posts/${postId}/tags/${tagId}`);
  }

  async addTags(postId: number, tagIDs: number[]): Promise<void> {
    await this.runRequestsInParallelForEachTagId<void>(this.addTag.bind(this), postId, tagIDs);
  }

  async removeTags(postId: number, tagIDs: number[]): Promise<void> {
    await this.runRequestsInParallelForEachTagId<void>(this.removeTag.bind(this), postId, tagIDs);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(env.apiUrl + 'posts/' + id).pipe(catchError(handleError));
  }

  publish(id: number): Observable<void> {
    return this.http
      .put<void>(`${env.apiUrl}posts/${id}/publish`, null)
      .pipe(catchError(handleError));
  }

  unpublish(id: number): Observable<void> {
    return this.http
      .put<void>(`${env.apiUrl}posts/${id}/unpublish`, null)
      .pipe(catchError(handleError));
  }

  private async runRequestsInParallelForEachTagId<T>(
    // eslint-disable-next-line
    fn: (...params: any[]) => Observable<T>,
    postId: number,
    tagsIDs: number[],
  ) {
    // Call the function fn for each tag id and convert the observers responses to promises
    const promises = tagsIDs.map(async (tagId) => await firstValueFrom(fn(postId, tagId)));
    // Will resolve only when all requests are finished (resolved)
    return Promise.all(promises);
  }
}
