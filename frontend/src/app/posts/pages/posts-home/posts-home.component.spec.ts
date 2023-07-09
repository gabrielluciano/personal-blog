import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { PostsHomeComponent } from './posts-home.component';
import { HeroComponent } from '../../../shared/components/hero/hero.component';
import { PostListComponent } from '../../components/post-list/post-list.component';
import { PostListItemComponent } from '../../components/post-list-item/post-list-item.component';
import { PillComponent } from 'src/app/shared/components/pill/pill.component';
import { RouterTestingModule } from '@angular/router/testing';
import { PostsService } from 'src/app/shared/services/posts.service';
import { of } from 'rxjs';
import { postsPageMock } from '../../../models/post/postsMock';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DateFormatPipe } from 'src/app/shared/pipes/date-format.pipe';
import { SharedModule } from 'src/app/shared/shared.module';
import { AuthService } from 'src/app/shared/services/auth.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

describe('PostsHomeComponent', () => {
  let component: PostsHomeComponent;
  let fixture: ComponentFixture<PostsHomeComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      list: of(postsPageMock),
    });
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['isEditor']);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, BrowserAnimationsModule, SharedModule, MatSlideToggleModule],
      declarations: [
        PostsHomeComponent,
        HeroComponent,
        PostListComponent,
        PostListItemComponent,
        PillComponent,
        DateFormatPipe,
      ],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
      ],
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

  it('should call slideChange method when toggle button is clicked', fakeAsync(() => {
    const slideChangeSpy = spyOn(component, 'onSlideChange');
    authServiceSpy.isEditor.and.returnValue(Promise.resolve(true));
    component.ngOnInit();

    tick();
    fixture.detectChanges();

    expect(component.drafts()).toBeFalsy();
    const button = fixture.elementRef.nativeElement.querySelector('mat-slide-toggle button');
    button.click();

    tick();

    expect(slideChangeSpy).toHaveBeenCalled();
  }));
});
