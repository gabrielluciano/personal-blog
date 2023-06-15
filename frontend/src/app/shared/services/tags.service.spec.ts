import { TestBed } from '@angular/core/testing';

import { TagsService } from './tags.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { tagsMock } from 'src/app/posts/tagsMock';

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

    const req = httpTestingController.expectOne('http://localhost:8080/tags/1');
    expect(req.request.method).toEqual('GET');
    req.flush(tagsMock[0]);
  });
});
