import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostComponent } from './post.component';
import { PostsService } from 'src/app/shared/services/posts.service';
import { of } from 'rxjs';
import { postsMock } from 'src/app/models/post/postsMock';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from 'src/app/shared/shared.module';

describe('PostComponent', () => {
  let component: PostComponent;
  let fixture: ComponentFixture<PostComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      findBySlug: of(postsMock[0]),
    });

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, SharedModule],
      declarations: [PostComponent],
      providers: [{ provide: PostsService, useValue: postsServiceSpy }],
    });
    fixture = TestBed.createComponent(PostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch PostResponse', () => {
    expect(component).toBeTruthy();
    expect(component.post.id).toBe(postsMock[0].id);
  });
});
