import { Component, Input, WritableSignal } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { PaginatorComponent } from '../../../shared/components/paginator/paginator.component';
import { PostListItemComponent } from '../post-list-item/post-list-item.component';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  standalone: true,
  imports: [PostListItemComponent, PaginatorComponent, MatProgressSpinner],
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
