import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostReponse } from 'src/app/models/post/postResponse';
import { AuthService } from 'src/app/shared/services/auth.service';
import { PostsService } from 'src/app/shared/services/posts.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
})
export class PostComponent implements OnInit {
  post!: PostReponse;
  editor = false;

  constructor(
    private postsService: PostsService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.params['slug'];
    this.findBySlug(slug);
    this.setEditor();
  }

  private findBySlug(slug: string) {
    this.postsService.findBySlug(slug).subscribe({
      next: (post) => (this.post = post),
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }

  private async setEditor() {
    this.editor = await this.authService.isEditor();
  }
}
