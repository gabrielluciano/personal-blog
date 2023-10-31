import { Component, Input } from '@angular/core';
import { PostReponse } from 'src/app/models/post/postResponse';

@Component({
  selector: 'app-post-list-item',
  templateUrl: './post-list-item.component.html',
})
export class PostListItemComponent {
  @Input() post!: PostReponse;
}
