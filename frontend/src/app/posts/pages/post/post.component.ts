import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostReponse } from 'src/app/models/post/postResponse';
import { PostsService } from 'src/app/shared/services/posts.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
})
export class PostComponent implements OnInit {
  post!: PostReponse;

  constructor(
    private postsService: PostsService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.params['slug'];
    this.findBySlug(slug);
  }

  findBySlug(slug: string) {
    this.postsService.findBySlug(slug).subscribe({
      next: (post) => (this.post = post),
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }
}
