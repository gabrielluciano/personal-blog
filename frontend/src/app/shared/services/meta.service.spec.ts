import { TestBed } from '@angular/core/testing';

import { MetaInfo, MetaService } from './meta.service';
import { Meta, Title } from '@angular/platform-browser';

describe('MetaService', () => {
  let service: MetaService;
  let metaSpy: jasmine.SpyObj<Meta>;
  let titleSpy: jasmine.SpyObj<Title>;

  beforeEach(() => {
    metaSpy = jasmine.createSpyObj<Meta>('Meta', ['getTag', 'addTag', 'updateTag']);
    titleSpy = jasmine.createSpyObj<Title>('Title', ['setTitle']);

    TestBed.configureTestingModule({
      providers: [
        { provide: Meta, useValue: metaSpy },
        { provide: Title, useValue: titleSpy },
      ],
    });
    service = TestBed.inject(MetaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set title tag and create og:title meta tag', () => {
    metaSpy.getTag.and.returnValue(null);

    const newTitle = 'New Title!';
    service.setTitle(newTitle);

    expect(titleSpy.setTitle).toHaveBeenCalledWith(newTitle);
    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:title'");
    expect(metaSpy.addTag).toHaveBeenCalledWith({ property: 'og:title', content: newTitle });
  });

  it('should set title tag and update og:title meta tag', () => {
    metaSpy.getTag.and.returnValue({ content: 'content' } as HTMLMetaElement);

    const newTitle = 'New Title!';
    service.setTitle(newTitle);

    expect(titleSpy.setTitle).toHaveBeenCalledWith(newTitle);
    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:title'");
    expect(metaSpy.updateTag).toHaveBeenCalledWith({ property: 'og:title', content: newTitle });
  });

  it('should create description and og:description meta tags', () => {
    metaSpy.getTag.and.returnValue(null);

    const newDescription = 'New Description!';
    service.setDescription(newDescription);

    expect(metaSpy.getTag).toHaveBeenCalledWith("name='description'");
    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:description'");
    expect(metaSpy.addTag).toHaveBeenCalledWith({ name: 'description', content: newDescription });
    expect(metaSpy.addTag).toHaveBeenCalledWith({
      property: 'og:description',
      content: newDescription,
    });
  });

  it('should update description and og:description meta tags', () => {
    metaSpy.getTag.and.returnValue({ content: 'content' } as HTMLMetaElement);

    const newDescription = 'New Description!';
    service.setDescription(newDescription);

    expect(metaSpy.getTag).toHaveBeenCalledWith("name='description'");
    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:description'");
    expect(metaSpy.updateTag).toHaveBeenCalledWith({
      name: 'description',
      content: newDescription,
    });
    expect(metaSpy.updateTag).toHaveBeenCalledWith({
      property: 'og:description',
      content: newDescription,
    });
  });

  it('should create og:image meta tag', () => {
    metaSpy.getTag.and.returnValue(null);

    const newImageUrl = 'http://example.com/image.jpg';
    service.setImage(newImageUrl);

    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:image'");
    expect(metaSpy.addTag).toHaveBeenCalledWith({ property: 'og:image', content: newImageUrl });
  });

  it('should update og:image meta tag', () => {
    metaSpy.getTag.and.returnValue({ content: 'content' } as HTMLMetaElement);

    const newImageUrl = 'http://example.com/image.jpg';
    service.setImage(newImageUrl);

    expect(metaSpy.getTag).toHaveBeenCalledWith("property='og:image'");
    expect(metaSpy.updateTag).toHaveBeenCalledWith({ property: 'og:image', content: newImageUrl });
  });

  it('should create meta tags', () => {
    metaSpy.getTag.and.returnValue(null);

    const newMetaInfo: MetaInfo = {
      title: 'New Title!',
      description: 'New Description',
      imageUrl: 'http://example.com/image.jpg',
    };
    service.setMetaInfo(newMetaInfo);

    expect(metaSpy.getTag).toHaveBeenCalledTimes(4);
    expect(titleSpy.setTitle).toHaveBeenCalledWith(newMetaInfo.title);
    expect(metaSpy.addTag).toHaveBeenCalledTimes(4);
  });

  it('should update meta tags', () => {
    metaSpy.getTag.and.returnValue({ content: 'content' } as HTMLMetaElement);

    const newMetaInfo: MetaInfo = {
      title: 'New Title!',
      description: 'New Description',
      imageUrl: 'http://example.com/image.jpg',
    };
    service.setMetaInfo(newMetaInfo);

    expect(metaSpy.getTag).toHaveBeenCalledTimes(4);
    expect(titleSpy.setTitle).toHaveBeenCalledWith(newMetaInfo.title);
    expect(metaSpy.updateTag).toHaveBeenCalledTimes(4);
  });
});
