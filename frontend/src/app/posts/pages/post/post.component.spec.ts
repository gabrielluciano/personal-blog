import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { provideRouter } from '@angular/router';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { of, throwError } from 'rxjs';
import { routes } from 'src/app/app.routes';
import { ErrorDetails } from 'src/app/models/errorDetails';
import { postsMock } from 'src/app/models/post/postsMock';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import {
  SnackbarComponent,
  getSnackBarDefaultConfig,
} from 'src/app/shared/components/snackbar/snackbar.component';
import { MetaService } from 'src/app/shared/services/meta.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { AppState } from 'src/app/shared/state/app.state';
import { initialState } from 'src/app/shared/state/auth/auth.reducer';
import { environment as env } from 'src/environments/environment';
import { PostComponent } from './post.component';

describe('PostComponent', () => {
  let component: PostComponent;
  let fixture: ComponentFixture<PostComponent>;
  let store: MockStore<AppState>;
  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let metaServiceSpy: jasmine.SpyObj<MetaService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<ConfirmDialogComponent>>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  const initialAppState: AppState = { auth: initialState };

  const errorDetailsMock: Partial<ErrorDetails> = {
    message: 'Ops we had an error!',
  };

  beforeEach(async () => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', [
      'findBySlug',
      'delete',
      'publish',
      'unpublish',
    ]);
    postsServiceSpy.findBySlug.and.returnValue(of(postsMock[0]));
    metaServiceSpy = jasmine.createSpyObj<MetaService>('MetaService', ['setMetaInfo']);
    dialogSpy = jasmine.createSpyObj<MatDialog>('MatDialog', ['open']);
    dialogRefSpy = jasmine.createSpyObj<MatDialogRef<ConfirmDialogComponent>>('MatDialogRef', [
      'afterClosed',
      'close',
    ]);
    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);

    await TestBed.configureTestingModule({
      imports: [PostComponent],
      providers: [
        provideRouter(routes),
        provideMockStore({ initialState: initialAppState }),
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: MetaService, useValue: metaServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(PostComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch PostResponse', () => {
    expect(component).toBeTruthy();
    expect(component.post.id).toBe(postsMock[0].id);
  });

  it('should call meta service after fetch post', () => {
    expect(component.post.id).toBe(postsMock[0].id);
    expect(metaServiceSpy.setMetaInfo).toHaveBeenCalledWith({
      title: postsMock[0].metaTitle,
      description: postsMock[0].metaDescription,
      imageUrl: postsMock[0].imageUrl + '-1024w.webp',
      canonicalUrl: `${env.siteUrl}posts/${postsMock[0].slug}`,
    });
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

  it('should call postsService delete method and call snackbar with success when delete dialog is confirmed', () => {
    postsServiceSpy.delete.and.returnValue(of(void 0));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the delete button to be available
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const deleteButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="delete"]',
    );
    deleteButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.delete).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig('Post excluído com sucesso', 'success'),
    );
  });

  it('should call postsService delete method and call snackbar with error when delete dialog is confirmed but an error happens', () => {
    postsServiceSpy.delete.and.returnValue(throwError(() => errorDetailsMock));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the delete button to be available
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const deleteButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="delete"]',
    );
    deleteButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.delete).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig(errorDetailsMock.message + '', 'error'),
    );
  });

  it('should call postsService publish method and call snackbar with success when publish dialog is confirmed', () => {
    postsServiceSpy.publish.and.returnValue(of(void 0));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the publish button to be available
    component.post = postsMock[1];
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const publishButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="publish"]',
    );
    publishButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.publish).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig('Post publicado com sucesso', 'success'),
    );
  });

  it('should call postsService publish method and call snackbar with error when publish dialog is confirmed but an error happens', () => {
    postsServiceSpy.publish.and.returnValue(throwError(() => errorDetailsMock));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the publish button to be available
    component.post = postsMock[1];
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const publishButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="publish"]',
    );
    publishButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.publish).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig(errorDetailsMock.message + '', 'error'),
    );
  });

  it('should call postsService unpublish method and call snackbar with success when unpublish dialog is confirmed', () => {
    postsServiceSpy.unpublish.and.returnValue(of(void 0));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the unpublish button to be available
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const unpublishButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="unpublished"]',
    );
    unpublishButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.unpublish).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig('Post despublicado com sucesso', 'success'),
    );
  });

  it('should call postsService unpublish method and call snackbar with error when unpublish dialog is confirmed but an error happens', () => {
    postsServiceSpy.unpublish.and.returnValue(throwError(() => errorDetailsMock));
    dialogSpy.open.and.returnValue(dialogRefSpy);
    dialogRefSpy.afterClosed.and.returnValue(of(true));

    // This is neccessary for the unpublish button to be available
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
        isEditor: true,
      },
    });
    fixture.detectChanges();

    const unpublishButton = fixture.elementRef.nativeElement.querySelector(
      'mat-icon[fonticon="unpublished"]',
    );
    unpublishButton.click();

    expect(dialogSpy.open).toHaveBeenCalled();
    expect(postsServiceSpy.unpublish).toHaveBeenCalled();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig(errorDetailsMock.message + '', 'error'),
    );
  });
});
