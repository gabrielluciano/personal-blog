import { ComponentFixture, TestBed } from '@angular/core/testing';

import { postsPageMock } from '../../../models/post/postsMock';
import { PostListItemComponent } from '../post-list-item/post-list-item.component';
import { PostListComponent } from './post-list.component';

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostListItemComponent],
    }).compileComponents();
    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    component.postsPage = postsPageMock;
    expect(component).toBeTruthy();
  });
});
