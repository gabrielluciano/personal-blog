import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { PostComponent } from './post.component';
import { PostsService } from 'src/app/shared/services/posts.service';
import { of } from 'rxjs';
import { postsMock } from 'src/app/models/post/postsMock';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from 'src/app/shared/shared.module';
import { AuthService } from 'src/app/shared/services/auth.service';

describe('PostComponent', () => {
  let component: PostComponent;
  let fixture: ComponentFixture<PostComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      findBySlug: of(postsMock[0]),
    });
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['isEditor']);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, SharedModule],
      declarations: [PostComponent],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
      ],
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

  it('should set editor to true when user is an editor', fakeAsync(() => {
    authServiceSpy.isEditor.and.returnValue(Promise.resolve(true));
    component.ngOnInit();
    tick();
    expect(component.editor).toBeTruthy();
  }));

  it('should set editor to false when user is not an editor', fakeAsync(() => {
    authServiceSpy.isEditor.and.returnValue(Promise.resolve(false));
    component.ngOnInit();
    tick();
    expect(component.editor).toBeFalsy();
  }));
});
