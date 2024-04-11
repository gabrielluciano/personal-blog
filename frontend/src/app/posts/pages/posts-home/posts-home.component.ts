import { AsyncPipe } from '@angular/common';
import { Component, effect, signal } from '@angular/core';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { MetaInfo, MetaService } from 'src/app/shared/services/meta.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { AppState } from 'src/app/shared/state/app.state';
import { selectAuthIsEditor } from 'src/app/shared/state/auth/auth.selectors';
import { environment as env } from 'src/environments/environment';
import { HeroComponent } from '../../../shared/components/hero/hero.component';
import { PostListComponent } from '../../components/post-list/post-list.component';

@Component({
  selector: 'app-posts-home',
  templateUrl: './posts-home.component.html',
  standalone: true,
  imports: [HeroComponent, MatSlideToggle, PostListComponent, AsyncPipe],
})
export class PostsHomeComponent {
  readonly META_INFO: MetaInfo = {
    title: 'gabrielluciano.com',
    description:
      'Bem vindo ao blog gabrielluciano.com. Aqui compartilho um pouco da minha paixão por tecnologia com você',
    imageUrl: env.siteUrl + 'assets/gabrielluciano-img.png',
    canonicalUrl: env.siteUrl + 'posts',
  };

  postsPage!: Page<PostReponse>;
  editor$: Observable<boolean>;
  pageSize = signal(10);
  pageIndex = signal(0);
  drafts = signal(false);

  constructor(
    private postsService: PostsService,
    private store: Store<AppState>,
    private metaService: MetaService,
  ) {
    this.metaService.setMetaInfo(this.META_INFO);
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
