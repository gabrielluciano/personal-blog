import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ErrorDetails } from 'src/app/models/errorDetails';
import { TagResponse } from 'src/app/models/tag/tagResponse';
import {
  SnackbarComponent,
  SnackbarData,
  getSnackBarDefaultConfig,
} from 'src/app/shared/components/snackbar/snackbar.component';
import { PostsService } from 'src/app/shared/services/posts.service';
import { TagsService } from 'src/app/shared/services/tags.service';
import { VALID_SLUG_PATTERN } from 'src/app/shared/util/regexPatterns';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss'],
})
export class PostFormComponent implements OnInit {
  readonly SUCCESSFUL_POST_CREATION_MESSAGE = 'Post created successfully';
  readonly SUCCESSFUL_POST_UPDATE_MESSAGE = 'Post updated successfully';
  isEdit = false;
  postId: null | number = null;
  initialIDsOfPostTags: number[] = [];
  form!: FormGroup;
  tags!: TagResponse[];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private postsService: PostsService,
    private tagsService: TagsService,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.isEdit = this.route.snapshot.data['edit'];
    this.getAllTags();
    this.createForm();
  }

  redirectToHome() {
    this.router.navigate(['/']);
  }

  async onSubmit() {
    try {
      await this.saveOrUpdatePostAndTags();
      // eslint-disable-next-line
    } catch (error: any) {
      this.handleSumissionError(error);
    }
  }

  private getAllTags() {
    this.tagsService.list().subscribe({
      next: (tags) => (this.tags = tags.content),
      error: (error) => this.showSnackBar(error.message, 'error'),
    });
  }

  private createForm() {
    this.form = this.fb.group({
      title: ['', [Validators.required]],
      subtitle: ['', [Validators.required]],
      slug: ['', [Validators.required, Validators.pattern(VALID_SLUG_PATTERN)]],
      metaTitle: ['', [Validators.required]],
      metaDescription: ['', [Validators.required]],
      imageUrl: ['', [Validators.required]],
      tags: [[], [Validators.required]],
      content: [''],
    });

    if (this.isEdit) this.findPostBySlugAndSetItsDataAndId();
  }

  private findPostBySlugAndSetItsDataAndId() {
    this.postsService.findBySlug(this.route.snapshot.params['slug']).subscribe({
      next: (post) => {
        this.postId = post.id;
        this.initialIDsOfPostTags = post.tags.map((tag) => tag.id);
        this.form.controls['title'].setValue(post.title);
        this.form.controls['subtitle'].setValue(post.subtitle);
        this.form.controls['slug'].setValue(post.slug);
        this.form.controls['metaTitle'].setValue(post.metaTitle);
        this.form.controls['metaDescription'].setValue(post.metaDescription);
        this.form.controls['imageUrl'].setValue(post.imageUrl);
        this.form.controls['tags'].setValue(this.initialIDsOfPostTags);
        this.form.controls['content'].setValue(post.content);
      },
      error: (error) => {
        this.showSnackBar(error.message, 'error');
        this.redirectToHome();
      },
    });
  }

  private async saveOrUpdatePostAndTags() {
    if (this.isEdit) {
      await this.updatePostAndTags();
    } else {
      await this.savePostAndTags();
    }
    this.redirectToHome();
  }

  private async updatePostAndTags() {
    const post = this.getPostFromForm();
    if (this.postId) {
      await firstValueFrom(this.postsService.update(post, this.postId));
      await this.updatePostTags(this.postId);
      this.showSnackBar(this.SUCCESSFUL_POST_UPDATE_MESSAGE, 'success');
    }
  }

  private async savePostAndTags() {
    const post = this.getPostFromForm();
    const savedPost = await firstValueFrom(this.postsService.save(post));
    await this.updatePostTags(savedPost.id);
    this.showSnackBar(this.SUCCESSFUL_POST_CREATION_MESSAGE, 'success');
  }

  private getPostFromForm() {
    const post = { ...this.form.value };
    delete post['tags'];
    return post;
  }

  private async updatePostTags(postId: number) {
    const currentIDsOfPostTags: number[] = this.form.value.tags;
    const tagsIDsToAdd = currentIDsOfPostTags.filter(
      (id) => !this.initialIDsOfPostTags.includes(id)
    );
    const tagsIDsToRemove = this.initialIDsOfPostTags.filter(
      (id) => !currentIDsOfPostTags.includes(id)
    );
    await this.postsService.addTags(postId, tagsIDsToAdd);
    await this.postsService.removeTags(postId, tagsIDsToRemove);
  }

  private handleSumissionError(error: ErrorDetails) {
    this.showSnackBar(error.message, 'error');
  }

  private showSnackBar(message: string, style: SnackbarData['style']) {
    this._snackBar.openFromComponent(SnackbarComponent, getSnackBarDefaultConfig(message, style));
  }
}
