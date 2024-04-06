import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs';
import { routes } from 'src/app/app.routes';
import { MetaService } from 'src/app/shared/services/meta.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { AppState } from 'src/app/shared/state/app.state';
import { initialState } from 'src/app/shared/state/auth/auth.reducer';
import { environment as env } from 'src/environments/environment';
import { postsPageMock } from '../../../models/post/postsMock';
import { PostsHomeComponent } from './posts-home.component';

describe('PostsHomeComponent', () => {
  let component: PostsHomeComponent;
  let fixture: ComponentFixture<PostsHomeComponent>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let metaServiceSpy: jasmine.SpyObj<MetaService>;
  let store: MockStore<AppState>;
  const initialAppState: AppState = { auth: initialState };

  beforeEach(async () => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', {
      list: of(postsPageMock),
    });
    metaServiceSpy = jasmine.createSpyObj<MetaService>('MetaService', ['setMetaInfo']);

    await TestBed.configureTestingModule({
      imports: [PostsHomeComponent],
      providers: [
        provideRouter(routes),
        provideAnimations(),
        provideMockStore({ initialState: initialAppState }),
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: MetaService, useValue: metaServiceSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PostsHomeComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call meta service', () => {
    expect(metaServiceSpy.setMetaInfo).toHaveBeenCalledWith({
      title: 'gabrielluciano.com',
      description:
        'Bem vindo ao blog gabrielluciano.com. Aqui compartilho um pouco da minha paixão por tecnologia com você',
      imageUrl: env.siteUrl + 'assets/gabrielluciano-img.png',
      canonicalUrl: env.siteUrl + 'posts',
    });
  });

  it('should fetch Page of PostResponse', () => {
    expect(component).toBeTruthy();
    expect(component.postsPage.numberOfElements).toBe(postsPageMock.content.length);
  });

  it('should set editor$ to Observable of false when user is not an editor', fakeAsync(() => {
    component.editor$.subscribe((value) => {
      expect(value).toBeFalsy();
    });
  }));

  it('should set editor$ to Observable of true when user is an editor', fakeAsync(() => {
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    tick();
    component.editor$.subscribe((value) => {
      expect(value).toBeTruthy();
    });
  }));

  it('should call slideChange method when toggle button is clicked', () => {
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    expect(component.drafts()).toBeFalsy();

    const button = fixture.elementRef.nativeElement.querySelector('mat-slide-toggle button');
    button.click();

    fixture.detectChanges();

    expect(component.drafts()).toBeTruthy();
    expect(postsServiceSpy.list).toHaveBeenCalledTimes(2);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(10, 0, null, true);
  });

  it('should call list method when pageSize value change', fakeAsync(() => {
    const pageSizeValue = 5;
    component.pageSize.set(pageSizeValue);

    tick();
    fixture.detectChanges();

    expect(postsServiceSpy.list).toHaveBeenCalledTimes(2);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(pageSizeValue, 0, null, false);
  }));

  it('should call list method when pageIndex value change', fakeAsync(() => {
    const pageIndexValue = 5;
    component.pageIndex.set(pageIndexValue);

    tick();
    fixture.detectChanges();

    expect(postsServiceSpy.list).toHaveBeenCalledTimes(2);
    expect(postsServiceSpy.list).toHaveBeenCalledWith(10, pageIndexValue, null, false);
  }));
});
