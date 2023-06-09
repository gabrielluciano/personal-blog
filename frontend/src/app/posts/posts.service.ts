import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../models/page';
import { PostReponse } from '../models/post/postResponse';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  constructor(private http: HttpClient) { }

  list(): Observable<Page<PostReponse>> {
    return this.http.get<Page<PostReponse>>("http://localhost:8080/posts");
  }
}
