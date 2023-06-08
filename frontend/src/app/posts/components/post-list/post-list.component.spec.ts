import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostListComponent } from './post-list.component';
import { PostListItemComponent } from '../post-list-item/post-list-item.component';
import { CustomDateComponent } from 'src/app/shared/custom-date/custom-date.component';
import { PillComponent } from 'src/app/shared/pill/pill.component';

describe('PostListComponent', () => {
  let component: PostListComponent;
  let fixture: ComponentFixture<PostListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PostListComponent, PostListItemComponent, CustomDateComponent, PillComponent]
    });
    fixture = TestBed.createComponent(PostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
