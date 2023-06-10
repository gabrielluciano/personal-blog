import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsHomeComponent } from './posts-home.component';
import { HeroComponent } from '../../components/hero/hero.component';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { CustomDateComponent } from 'src/app/shared/components/custom-date/custom-date.component';
import { PillComponent } from 'src/app/shared/components/pill/pill.component';
import { RouterTestingModule } from '@angular/router/testing';
import { PostsService } from '../../posts.service';
import { of } from 'rxjs';
import { postsPageMock } from '../../postsPageMock';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('PostsHomeComponent', () => {
  let component: PostsHomeComponent;
  let fixture: ComponentFixture<PostsHomeComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      list: of(postsPageMock)
    });

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, BrowserAnimationsModule, MatPaginatorModule],
      declarations: [PostsHomeComponent, HeroComponent, PostListComponent, PostListItemComponent, CustomDateComponent, PillComponent],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy }
      ]
    });
    fixture = TestBed.createComponent(PostsHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch Page of PostResponse', () => {
    expect(component).toBeTruthy();
    expect(component.postsPage.numberOfElements).toBe(postsPageMock.content.length);
  })
});
