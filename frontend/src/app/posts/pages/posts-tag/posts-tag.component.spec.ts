import { TestBed } from '@angular/core/testing';

import { of, throwError } from 'rxjs';

import { PostsTagComponent } from './posts-tag.component';
import { TagsService } from 'src/app/shared/services/tags.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { postsPageMock } from '../../../models/post/postsMock';
import { tagsMock } from '../../../models/tag/tagsMock';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { MetaService } from 'src/app/shared/services/meta.service';

describe('PostsTagComponent', () => {
  let component: PostsTagComponent;
  let harness: RouterTestingHarness;
  let router: Router;

  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let tagsServiceSpy: jasmine.SpyObj<TagsService>;
  let metaServiceSpy: jasmine.SpyObj<MetaService>;

  async function createComponent(id: number) {
    harness = await RouterTestingHarness.create();
    component = await harness.navigateByUrl(`/posts/tag/${id}/slug`, PostsTagComponent);
  }

  beforeEach(async () => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', ['list']);
    tagsServiceSpy = jasmine.createSpyObj<TagsService>('TagsService', ['findById']);
    metaServiceSpy = jasmine.createSpyObj<MetaService>('MetaService', ['setMetaInfo']);

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'posts/tag/:tagId/:tagSlug', component: PostsTagComponent },
        ]),
        BrowserAnimationsModule,
        SharedModule,
      ],
      declarations: [PostsTagComponent, PostListComponent, PostListItemComponent],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: TagsService, useValue: tagsServiceSpy },
        { provide: MetaService, useValue: metaServiceSpy },
      ],
    });
    router = TestBed.inject(Router);
  });

  it('should create', async () => {
    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.and.returnValue(of(tagsMock[0]));
    await createComponent(1);
    expect(component).toBeTruthy();
  });

  it('should call meta service after load tag', async () => {
    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.and.returnValue(of(tagsMock[0]));
    await createComponent(1);
    expect(metaServiceSpy.setMetaInfo).toHaveBeenCalledWith({
      title: 'Posts sobre ' + tagsMock[0].name,
      description: tagsMock[0].description,
      imageUrl: 'assets/gabrielluciano-img.png',
    });
  });

  it('should load tag and posts by tag id', async () => {
    const id = 2;
    const expectedTag = tagsMock[id - 1];

    expect(expectedTag.id).toEqual(id);

    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.withArgs(id).and.returnValue(of(expectedTag));

    await createComponent(id);

    expect(component).toBeTruthy();
    expect(tagsServiceSpy.findById).toHaveBeenCalledWith(id);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(10, 0, id);
  });

  it('should load tag and posts by tag id', async () => {
    const id = 2;
    const expectedTag = tagsMock[id - 1];

    expect(expectedTag.id).toEqual(id);

    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.withArgs(id).and.returnValue(of(expectedTag));

    await createComponent(id);

    expect(component).toBeTruthy();
    expect(tagsServiceSpy.findById).toHaveBeenCalledOnceWith(id);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(10, 0, id);
  });

  it("should navigate to home when tag doesn't exist", async () => {
    const routerNavigateSpy = spyOn(router, 'navigate');

    const id = 2;

    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.withArgs(id).and.returnValue(throwError(() => 'tag not found'));

    await createComponent(id);

    expect(component).toBeTruthy();
    expect(routerNavigateSpy).toHaveBeenCalledOnceWith(['/']);
  });

  it('should call list method when pageSize value change', async () => {
    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.and.returnValue(of(tagsMock[0]));

    await createComponent(1);

    expect(component).toBeTruthy();

    const pageSizeValue = 5;
    component.pageSize.set(pageSizeValue);

    harness.detectChanges();

    expect(postsServiceSpy.list).toHaveBeenCalledTimes(2);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(pageSizeValue, 0, tagsMock[0].id);
  });

  it('should call list method when pageIndex value change', async () => {
    postsServiceSpy.list.and.returnValue(of(postsPageMock));
    tagsServiceSpy.findById.and.returnValue(of(tagsMock[0]));

    await createComponent(1);

    expect(component).toBeTruthy();

    const pageIndexValue = 2;
    component.pageIndex.set(pageIndexValue);

    harness.detectChanges();

    expect(postsServiceSpy.list).toHaveBeenCalledTimes(2);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(10, pageIndexValue, tagsMock[0].id);
  });
});
