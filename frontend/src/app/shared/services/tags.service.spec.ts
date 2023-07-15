import { TestBed } from '@angular/core/testing';

import { TagsService } from './tags.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { tagsMock, tagsPageMock } from 'src/app/models/tag/tagsMock';
import { environment as env } from 'src/environments/environment';

describe('TagsService', () => {
  let service: TagsService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TagsService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a TagReponse', () => {
    service.findById(tagsMock[0].id).subscribe((tag) => {
      expect(tag).toBeTruthy();
      expect(tag.id).toEqual(tagsMock[0].id);
    });

    const req = httpTestingController.expectOne(env.apiUrl + 'tags/1');
    expect(req.request.method).toEqual('GET');
    req.flush(tagsMock[0]);
  });

  it('should return a Page of TagReponse', () => {
    service.list().subscribe((page) => {
      expect(page).toBeTruthy();
      expect(page.content.length).toEqual(tagsPageMock.content.length);
    });

    const req = httpTestingController.expectOne(env.apiUrl + 'tags?size=100');
    expect(req.request.method).toEqual('GET');
    req.flush(tagsPageMock);
  });
});
