import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ErrorDetails } from 'src/app/models/errorDetails';
import { PostReponse } from 'src/app/models/post/postResponse';
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
  form!: FormGroup;
  tags!: TagResponse[];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private postsService: PostsService,
    private tagsService: TagsService,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.getTags();
  }

  onCancel() {
    this.router.navigate(['/']);
  }

  async onSubmit() {
    const post = { ...this.form.value };
    delete post['tags'];

    try {
      await this.savePostAndTags(post);
      // eslint-disable-next-line
    } catch (error: any) {
      this.handleSumissionError(error);
    }
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
  }

  private getTags() {
    this.tagsService.list().subscribe({
      next: (tags) => (this.tags = tags.content),
      error: (error) => this.showSnackBar(error.message, 'error'),
    });
  }

  private async savePostAndTags(post: PostReponse) {
    const savedPost = await firstValueFrom(this.postsService.save(post));
    await this.addTags(savedPost.id);
    this.showSnackBar(this.SUCCESSFUL_POST_CREATION_MESSAGE, 'success');
    this.onCancel();
  }

  private async addTags(postId: number) {
    const tags: number[] = this.form.value.tags;
    if (tags) {
      // Call the API endpoint for each tag id and convert the observers responses to promises
      const promises = tags.map(async (tagId) => {
        await firstValueFrom(this.postsService.addTag(postId, tagId));
      });
      // Will resolve only when all requests are resolved
      return Promise.all(promises);
    }
    return Promise.resolve();
  }

  private handleSumissionError(error: ErrorDetails) {
    this.showSnackBar(error.message, 'error');
  }

  private showSnackBar(message: string, style: SnackbarData['style']) {
    this._snackBar.openFromComponent(SnackbarComponent, getSnackBarDefaultConfig(message, style));
  }
}
