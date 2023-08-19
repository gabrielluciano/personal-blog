import { Component, signal, effect, WritableSignal, OnDestroy } from '@angular/core';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { PostsService } from 'src/app/shared/services/posts.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TagsService } from 'src/app/shared/services/tags.service';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import { Subject, takeUntil } from 'rxjs';
import { MetaService } from 'src/app/shared/services/meta.service';

@Component({
  selector: 'app-posts-tag',
  templateUrl: './posts-tag.component.html',
  styleUrls: ['./posts-tag.component.scss'],
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
    private metaService: MetaService
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
          imageUrl: 'assets/gabrielluciano-img.png',
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
