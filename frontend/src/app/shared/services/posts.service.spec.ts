import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PostsService } from './posts.service';
import { postsMock, postsPageMock } from 'src/app/models/post/postsMock';
import { PostCreateRequest } from 'src/app/models/post/postCreateRequest';
import { PostUpdateRequest } from 'src/app/models/post/postUpdateRequest';
import { environment as env } from 'src/environments/environment';

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
      `${env.apiUrl}posts?tag=&sort=createdAt,desc&size=5&page=0&drafts=false`,
    );
    expect(req.request.method).toEqual('GET');
    req.flush(postsPageMock);
  });

  it('should return a PostReponse', () => {
    service.findBySlug(postsMock[0].slug).subscribe((post) => {
      expect(post).toBeTruthy();
      expect(post.slug).toEqual(postsMock[0].slug);
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/slug/${postsMock[0].slug}`);
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

    const req = httpTestingController.expectOne(`${env.apiUrl}posts`);
    expect(req.request.method).toEqual('POST');
    req.flush(postsMock[0]);
  });

  it('should return a 204 when Post is updated', () => {
    const postUpdateRequest: PostUpdateRequest = {
      title: postsMock[0].title,
      imageUrl: postsMock[0].imageUrl,
      metaDescription: postsMock[0].metaDescription,
      metaTitle: postsMock[0].metaTitle,
      slug: postsMock[0].slug,
      subtitle: postsMock[0].subtitle,
      content: postsMock[0].content,
    };

    service.update(postUpdateRequest, postsMock[0].id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${postsMock[0].id}`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when tag is added to Post', () => {
    const postId = 1;
    const tagId = 1;

    service.addTag(postId, tagId).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${tagId}`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when tag is removed from Post', () => {
    const postId = 1;
    const tagId = 1;

    service.removeTag(postId, tagId).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${tagId}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should resolve the promise with void value when multiple tags are added to post', async () => {
    const postId = 1;

    const result = service.addTags(postId, [1, 2]);

    const req1 = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${1}`);
    const req2 = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${2}`);
    expect(req1.request.method).toEqual('PUT');
    expect(req2.request.method).toEqual('PUT');
    req1.flush(null, { status: 204, statusText: 'No Content' });
    req2.flush(null, { status: 204, statusText: 'No Content' });

    await expectAsync(result).toBeResolved();
  });

  it('should resolve the promise with void value when multiple tags are removed from post', async () => {
    const postId = 1;

    const result = service.removeTags(postId, [1, 2]);

    const req1 = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${1}`);
    const req2 = httpTestingController.expectOne(`${env.apiUrl}posts/${postId}/tags/${2}`);
    expect(req1.request.method).toEqual('DELETE');
    expect(req2.request.method).toEqual('DELETE');
    req1.flush(null, { status: 204, statusText: 'No Content' });
    req2.flush(null, { status: 204, statusText: 'No Content' });

    await expectAsync(result).toBeResolved();
  });

  it('should return status 204 when post is deleted', () => {
    const id = 1;
    service.delete(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${id}`);
    expect(req.request.method).toEqual('DELETE');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when post is published', () => {
    const id = 1;
    service.publish(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${id}/publish`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('should return status 204 when post is unpublished', () => {
    const id = 1;
    service.unpublish(id).subscribe((result) => {
      expect(result).toBeNull();
    });

    const req = httpTestingController.expectOne(`${env.apiUrl}posts/${id}/unpublish`);
    expect(req.request.method).toEqual('PUT');
    req.flush(null, { status: 204, statusText: 'No Content' });
  });
});
