import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsComponent } from './posts.component';
import { HeroComponent } from '../../components/hero/hero.component';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { CustomDateComponent } from 'src/app/shared/custom-date/custom-date.component';
import { PillComponent } from 'src/app/shared/pill/pill.component';

describe('PostsComponent', () => {
  let component: PostsComponent;
  let fixture: ComponentFixture<PostsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PostsComponent, HeroComponent, PostListComponent, PostListItemComponent, CustomDateComponent, PillComponent]
    });
    fixture = TestBed.createComponent(PostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
