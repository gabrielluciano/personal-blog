import { Component, Input, WritableSignal } from '@angular/core';
import { PostReponse } from 'src/app/models/post/postResponse';
import { Page } from 'src/app/models/page';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss'],
})
export class PostListComponent {
  @Input() postsPage!: Page<PostReponse>;
  @Input() pageSize!: WritableSignal<number>;
  @Input() pageIndex!: WritableSignal<number>;

  onPageEvent(pageEvent: PageEvent) {
    this.pageSize.set(pageEvent.pageSize);
    this.pageIndex.set(pageEvent.pageIndex);
  }
}
