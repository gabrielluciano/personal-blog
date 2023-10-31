import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { PostFormComponent } from './post-form.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PostsService } from 'src/app/shared/services/posts.service';
import { TagsService } from 'src/app/shared/services/tags.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { tagsPageMock } from 'src/app/models/tag/tagsMock';
import { Router } from '@angular/router';
import { postsMock } from 'src/app/models/post/postsMock';
import {
  SnackbarComponent,
  getSnackBarDefaultConfig,
} from 'src/app/shared/components/snackbar/snackbar.component';

describe('PostFormComponent', () => {
  let component: PostFormComponent;
  let fixture: ComponentFixture<PostFormComponent>;
  let router: Router;

  let postsServiceSpy: jasmine.SpyObj<PostsService>;
  let tagsServiceSpy: jasmine.SpyObj<TagsService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;

  beforeEach(() => {
    postsServiceSpy = jasmine.createSpyObj<PostsService>('PostsService', [
      'save',
      'addTags',
      'removeTags',
    ]);
    postsServiceSpy.save.and.returnValue(of(postsMock[0]));
    postsServiceSpy.addTags.and.returnValue(Promise.resolve());
    postsServiceSpy.removeTags.and.returnValue(Promise.resolve());

    tagsServiceSpy = jasmine.createSpyObj<TagsService>('TagsService', ['list']);
    tagsServiceSpy.list.and.returnValue(of(tagsPageMock));

    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
      declarations: [PostFormComponent],
      providers: [
        { provide: PostsService, useValue: postsServiceSpy },
        { provide: TagsService, useValue: tagsServiceSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ],
    });
    fixture = TestBed.createComponent(PostFormComponent);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call router router navigate when cancel button is clicked', () => {
    const routerNavigateSpy = spyOn(router, 'navigate');
    const button = fixture.elementRef.nativeElement.querySelector('button[color="accent"]');
    button.click();

    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);
  });

  it('should save post, add tags and navigate to home when onSubmit is called and form is valid', fakeAsync(() => {
    const routerNavigateSpy = spyOn(router, 'navigate');

    component.form.controls['title'].setValue('Title');
    component.form.controls['subtitle'].setValue('subtitle');
    component.form.controls['slug'].setValue('slug');
    component.form.controls['metaTitle'].setValue('meta title');
    component.form.controls['metaDescription'].setValue('meta description');
    component.form.controls['imageUrl'].setValue('http://imageurl.com/image.jpg');
    component.form.controls['tags'].setValue([1, 2]);
    component.form.controls['content'].setValue('content');

    expect(component.form.valid).toBeTruthy();
    const button = fixture.elementRef.nativeElement.querySelector('button[color="primary"]');
    button.removeAttribute('disabled');
    button.click();

    tick();

    expect(postsServiceSpy.save).toHaveBeenCalled();
    expect(postsServiceSpy.addTags).toHaveBeenCalledTimes(1);
    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);
  }));

  it('should throw an error when posts service save is called and call snackbar with error message', fakeAsync(() => {
    const errorMsg = 'Error!';
    postsServiceSpy.save.and.throwError(errorMsg);

    component.form.controls['title'].setValue('Title');
    component.form.controls['subtitle'].setValue('subtitle');
    component.form.controls['slug'].setValue('slug');
    component.form.controls['metaTitle'].setValue('meta title');
    component.form.controls['metaDescription'].setValue('meta description');
    component.form.controls['imageUrl'].setValue('http://imageurl.com/image.jpg');
    component.form.controls['tags'].setValue([1, 2]);
    component.form.controls['content'].setValue('content');

    expect(component.form.valid).toBeTruthy();
    const button = fixture.elementRef.nativeElement.querySelector('button[color="primary"]');
    button.removeAttribute('disabled');
    button.click();

    tick();

    expect(postsServiceSpy.save).toThrowError();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalledWith(
      SnackbarComponent,
      getSnackBarDefaultConfig(errorMsg, 'error'),
    );
  }));
});
