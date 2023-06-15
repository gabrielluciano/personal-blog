import { ComponentFixture, TestBed } from '@angular/core/testing';

import { of } from 'rxjs';

import { PostsTagComponent } from './posts-tag.component';
import { TagsService } from 'src/app/shared/services/tags.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { postsPageMock } from '../../postsPageMock';
import { tagsMock } from '../../tagsMock';
import { RouterTestingModule } from '@angular/router/testing';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('PostsTagComponent', () => {
  let component: PostsTagComponent;
  let fixture: ComponentFixture<PostsTagComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let tagsServiceSpy: jasmine.SpyObj<TagsService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      list: of(postsPageMock),
    });

    tagsServiceSpy = jasmine.createSpyObj<TagsService>('TagsService', {
      findById: of(tagsMock[0]),
    });

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, BrowserAnimationsModule, SharedModule],
      declarations: [PostsTagComponent, PostListComponent, PostListItemComponent],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: TagsService, useValue: tagsServiceSpy },
      ],
    });
    fixture = TestBed.createComponent(PostsTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
