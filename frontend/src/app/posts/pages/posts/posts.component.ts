import { Component, OnInit } from '@angular/core';
import { PostsService } from '../../posts.service';
import { Page } from 'src/app/models/page';
import { PostReponse } from 'src/app/models/post/postResponse';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
  postsPage!: Page<PostReponse>;

  constructor(private postsService: PostsService) { }

  ngOnInit(): void {
    this.list();
  }

  list() {
    this.postsService.list().subscribe(page => this.postsPage = page);
  }
}
