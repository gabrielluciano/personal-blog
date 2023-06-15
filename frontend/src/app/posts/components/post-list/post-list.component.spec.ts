import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { PostListComponent } from './post-list.component';
import { PostListItemComponent } from '../post-list-item/post-list-item.component';
import { PillComponent } from 'src/app/shared/components/pill/pill.component';
import { RouterTestingModule } from '@angular/router/testing';
import { postsPageMock } from '../../../models/post/postsPageMock';

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, MatProgressSpinnerModule],
      declarations: [PostListComponent, PostListItemComponent, PillComponent],
    });
    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    component.postsPage = postsPageMock;
    expect(component).toBeTruthy();
  });
});
