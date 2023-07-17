import { Component, effect, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { PostsService } from 'src/app/shared/services/posts.service';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/shared/state/app.state';
import { selectAuthIsEditor } from 'src/app/shared/state/auth/auth.selectors';

@Component({
  selector: 'app-posts-home',
  templateUrl: './posts-home.component.html',
  styleUrls: ['./posts-home.component.scss'],
})
export class PostsHomeComponent {
  postsPage!: Page<PostReponse>;
  editor$: Observable<boolean>;
  pageSize = signal(10);
  pageIndex = signal(0);
  drafts = signal(false);

  constructor(private postsService: PostsService, private store: Store<AppState>) {
    this.editor$ = store.select(selectAuthIsEditor);
    effect(() => {
      this.list(this.pageSize(), this.pageIndex(), this.drafts());
    });
  }

  list(pageSize: number, pageIndex: number, drafts: boolean) {
    this.postsService
      .list(pageSize, pageIndex, null, drafts)
      .subscribe((page) => (this.postsPage = page));
  }

  onSlideChange() {
    this.drafts.update((v) => !v);
  }
}
