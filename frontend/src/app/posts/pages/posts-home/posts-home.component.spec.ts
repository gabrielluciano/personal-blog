import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsHomeComponent } from './posts-home.component';
import { HeroComponent } from '../../../shared/components/hero/hero.component';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { PillComponent } from 'src/app/shared/components/pill/pill.component';
import { RouterTestingModule } from '@angular/router/testing';
import { PostsService } from 'src/app/shared/services/posts.service';
import { of } from 'rxjs';
import { postsPageMock } from '../../postsPageMock';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DateFormatPipe } from 'src/app/shared/pipes/date-format.pipe';
import { SharedModule } from 'src/app/shared/shared.module';

describe('PostsHomeComponent', () => {
  let component: PostsHomeComponent;
  let fixture: ComponentFixture<PostsHomeComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      list: of(postsPageMock),
    });

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, BrowserAnimationsModule, SharedModule],
      declarations: [
        PostsHomeComponent,
        HeroComponent,
        PostListComponent,
        PostListItemComponent,
        PillComponent,
        DateFormatPipe,
      ],
      providers: [{ provide: PostsService, useValue: postsServiceSpy }],
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
  });
});
