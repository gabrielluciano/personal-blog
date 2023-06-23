import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PostsService } from './posts.service';
import { postsPageMock } from '../../models/post/postsPageMock';
import { postsMock } from 'src/app/models/post/postsMock';

describe('PostsService', () => {
  let service: PostsService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(PostsService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a Page of PostReponse', () => {
    service.list(5, 0).subscribe((page) => {
      expect(page).toBeTruthy();
      expect(page.content.length).toEqual(postsPageMock.content.length);
    });

    const req = httpTestingController.expectOne(
      'http://localhost:8080/posts?tag=&sort=createdAt,desc&size=5&page=0'
    );
    expect(req.request.method).toEqual('GET');
    req.flush(postsPageMock);
  });

  it('should return a PostReponse', () => {
    service.findBySlug(postsMock[0].slug).subscribe((post) => {
      expect(post).toBeTruthy();
      expect(post.slug).toEqual(postsMock[0].slug);
    });

    const req = httpTestingController.expectOne(
      `http://localhost:8080/posts/slug/${postsMock[0].slug}`
    );
    expect(req.request.method).toEqual('GET');
    req.flush(postsMock[0]);
  });
});