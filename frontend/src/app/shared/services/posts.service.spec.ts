import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PostsService } from './posts.service';
import { postsMock, postsPageMock } from 'src/app/models/post/postsMock';
import { PostCreateRequest } from 'src/app/models/post/postCreateRequest';

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
    service.list(5, 0, null).subscribe((page) => {
      expect(page).toBeTruthy();
      expect(page.content.length).toEqual(postsPageMock.content.length);
    });

    const req = httpTestingController.expectOne(
      'http://localhost:8080/posts?tag=&sort=createdAt,desc&size=5&page=0&drafts=false'
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

  it('should return a PostReponse when Post is created', () => {
    const postCreateRequest: PostCreateRequest = {
      title: postsMock[0].title,
      imageUrl: postsMock[0].imageUrl,
      metaDescription: postsMock[0].metaDescription,
      metaTitle: postsMock[0].metaTitle,
      slug: postsMock[0].slug,
      subtitle: postsMock[0].subtitle,
      content: postsMock[0].content,
    };

    service.save(postCreateRequest).subscribe((post) => {
      expect(post).toBeTruthy();
      expect(post.title).toEqual(postCreateRequest.title);
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/posts`);
    expect(req.request.method).toEqual('POST');
    req.flush(postsMock[0]);
  });

  it('should return status 204 when tag is added to Post', () => {
    const postId = 1;
    const tagId = 1;

    service.addTag(postId, tagId).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(
      `http://localhost:8080/posts/${postId}/tags/${tagId}`
    );
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when post is deleted', () => {
    const id = 1;
    service.delete(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/posts/${id}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when post is published', () => {
    const id = 1;
    service.publish(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/posts/${id}/publish`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when post is unpublished', () => {
    const id = 1;
    service.unpublish(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`http://localhost:8080/posts/${id}/unpublish`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });
});
