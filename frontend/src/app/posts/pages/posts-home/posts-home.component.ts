import { Component, effect, signal } from '@angular/core';
import { PostsService } from '../../posts.service';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';

@Component({
  selector: 'app-posts-home',
  templateUrl: './posts-home.component.html',
  styleUrls: ['./posts-home.component.scss'],
})
export class PostsHomeComponent {
  postsPage!: Page<PostReponse>;
  pageSize = signal(10);
  pageIndex = signal(0);

  constructor(private postsService: PostsService) {
    effect(() => {
      this.list(this.pageSize(), this.pageIndex());
    });
  }

  list(pageSize: number, pageIndex: number) {
    this.postsService.list(pageSize, pageIndex).subscribe((page) => (this.postsPage = page));
  }
}
