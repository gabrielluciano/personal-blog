import { Component, signal, effect } from '@angular/core';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';
import { PostsService } from 'src/app/shared/services/posts.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TagsService } from 'src/app/shared/services/tags.service';
import { TagResponse } from 'src/app/models/tag/tagResponse';

@Component({
  selector: 'app-posts-tag',
  templateUrl: './posts-tag.component.html',
  styleUrls: ['./posts-tag.component.scss'],
})
export class PostsTagComponent {
  postsPage!: Page<PostReponse>;
  tag!: TagResponse;
  pageSize = signal(10);
  pageIndex = signal(0);

  constructor(
    private postsService: PostsService,
    private tagsService: TagsService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    const tagId = +this.route.snapshot.params['tagId'];
    this.findTagByIdOrRedirectToHome(tagId);

    effect(() => {
      this.list(this.pageSize(), this.pageIndex(), tagId);
    });
  }

  list(pageSize: number, pageIndex: number, tagId: number) {
    this.postsService.list(pageSize, pageIndex, tagId).subscribe((page) => (this.postsPage = page));
  }

  findTagByIdOrRedirectToHome(tagId: number) {
    this.tagsService.findById(tagId).subscribe({
      next: (tag) => (this.tag = tag),
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }
}
