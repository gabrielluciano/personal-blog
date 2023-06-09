import { Component, Input } from '@angular/core';
import { PostReponse } from 'src/app/models/post/postResponse';
import { Page } from 'src/app/models/page';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent {
  @Input() postsPage!: Page<PostReponse>;
}
