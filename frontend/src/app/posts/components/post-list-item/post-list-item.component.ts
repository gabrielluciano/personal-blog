import { Component, Input } from '@angular/core';
import { PostReponse } from 'src/app/models/post/postResponse';

@Component({
  selector: 'app-post-list-item',
  templateUrl: './post-list-item.component.html',
  styleUrls: ['./post-list-item.component.scss'],
})
export class PostListItemComponent {
  @Input() post!: PostReponse;
}
