import { NgIf } from '@angular/common';
import { Component, OnDestroy, WritableSignal, effect, signal } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import { MetaService } from 'src/app/shared/services/meta.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { TagsService } from 'src/app/shared/services/tags.service';
import { environment as env } from 'src/environments/environment';
import { PostListComponent } from '../../components/post-list/post-list.component';

@Component({
  selector: 'app-posts-tag',
  templateUrl: './posts-tag.component.html',
  standalone: true,
  imports: [NgIf, PostListComponent],
})
export class PostsTagComponent implements OnDestroy {
  private destroy$ = new Subject<void>();

  postsPage!: Page<PostReponse>;
  tag!: TagResponse;
  tagId: WritableSignal<number | null> = signal(null);
  pageSize = signal(10);
  pageIndex = signal(0);

  constructor(
    private postsService: PostsService,
    private tagsService: TagsService,
    private route: ActivatedRoute,
    private router: Router,
    private metaService: MetaService,
  ) {
    this.getTagIdFromRouteAndLoadTag();

    effect(() => {
      this.list(this.pageSize(), this.pageIndex(), this.tagId());
    });
  }

  private getTagIdFromRouteAndLoadTag() {
    this.route.params.pipe(takeUntil(this.destroy$)).subscribe((params: Params) => {
      const tagId = +params['tagId'];
      this.tagId.set(tagId);
      this.findTagByIdOrRedirectToHome(tagId);
    });
  }

  private list(pageSize: number, pageIndex: number, tagId: number | null) {
    this.postsService.list(pageSize, pageIndex, tagId).subscribe((page) => (this.postsPage = page));
  }

  private findTagByIdOrRedirectToHome(tagId: number) {
    this.tagsService.findById(tagId).subscribe({
      next: (tag) => {
        this.tag = tag;
        this.metaService.setMetaInfo({
          title: 'Posts sobre ' + tag.name,
          description: tag.description,
          imageUrl: env.siteUrl + 'assets/gabrielluciano-img.png',
          canonicalUrl: `${env.siteUrl}posts/tag/${tag.id}/${tag.slug}`,
        });
      },
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
