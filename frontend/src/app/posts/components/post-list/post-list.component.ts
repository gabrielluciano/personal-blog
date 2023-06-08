import { Component } from '@angular/core';
import { PostReponse } from 'src/app/models/post/postResponse';
import { posts } from '../../posts';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent {
  posts: PostReponse[] = posts;
}
