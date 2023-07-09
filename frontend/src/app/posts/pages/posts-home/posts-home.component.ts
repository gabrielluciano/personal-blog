import { Component, OnInit, effect, signal } from '@angular/core';
import { PostsService } from 'src/app/shared/services/posts.service';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-posts-home',
  templateUrl: './posts-home.component.html',
  styleUrls: ['./posts-home.component.scss'],
})
export class PostsHomeComponent implements OnInit {
  postsPage!: Page<PostReponse>;
  editor = false;
  pageSize = signal(10);
  pageIndex = signal(0);
  drafts = signal(false);

  constructor(private postsService: PostsService, private authService: AuthService) {
    effect(() => {
      this.list(this.pageSize(), this.pageIndex(), this.drafts());
    });
  }

  ngOnInit(): void {
    this.setEditor();
  }

  list(pageSize: number, pageIndex: number, drafts: boolean) {
    this.postsService
      .list(pageSize, pageIndex, null, drafts)
      .subscribe((page) => (this.postsPage = page));
  }

  onSlideChange() {
    this.drafts.update((v) => !v);
  }

  private async setEditor() {
    this.editor = await this.authService.isEditor();
  }
}
