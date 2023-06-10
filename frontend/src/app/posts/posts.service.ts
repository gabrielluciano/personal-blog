import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../models/page';
import { PostReponse } from '../models/post/postResponse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  DEFAULT_PAGE_SIZE = 10;
  DEFAULT_PAGE_INDEX = 0;

  constructor(private http: HttpClient) { }

  list(pageSize: number, pageIndex: number): Observable<Page<PostReponse>> {
    if (!pageSize && pageSize != 0) {
      pageSize = this.DEFAULT_PAGE_SIZE;
    }

    if (!pageIndex && pageIndex != 0) {
      pageIndex = this.DEFAULT_PAGE_INDEX;
    }

    return this.http.get<Page<PostReponse>>("http://localhost:8080/posts", {
      params: new HttpParams().set("size", pageSize).set("page", pageIndex)
    });
  }
}
