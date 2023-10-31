import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import msg, { SUCCESS_PUBLISH_POST_MSG } from 'src/app/i18n/pt/msg';
import { PostReponse } from 'src/app/models/post/postResponse';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import { showSnackBar } from 'src/app/shared/components/snackbar/snackbar.component';
import { MetaService } from 'src/app/shared/services/meta.service';
import { PostsService } from 'src/app/shared/services/posts.service';
import { AppState } from 'src/app/shared/state/app.state';
import { selectAuthIsEditor } from 'src/app/shared/state/auth/auth.selectors';

type CrudOperation = 'edit' | 'delete' | 'publish' | 'unpublish';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
})
export class PostComponent implements OnInit {
  post!: PostReponse;
  editor$: Observable<boolean>;

  constructor(
    private postsService: PostsService,
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<AppState>,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar,
    private metaService: MetaService,
  ) {
    this.editor$ = store.select(selectAuthIsEditor);
  }

  ngOnInit(): void {
    const slug = this.route.snapshot.params['slug'];
    this.findBySlug(slug);
  }

  openDialog(operation: CrudOperation) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      panelClass: 'dialog',
      data: {
        message: this.getDialogMessage(operation),
      },
    });

    dialogRef.afterClosed().subscribe((isConfirm) => {
      if (isConfirm) this.callOperationMethod(operation);
    });
  }

  private findBySlug(slug: string) {
    this.postsService.findBySlug(slug).subscribe({
      next: (post) => {
        this.post = post;
        this.metaService.setMetaInfo({
          title: post.metaTitle,
          description: post.metaDescription,
          imageUrl: post.imageUrl + '-500w.png',
        });
      },
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }

  private callOperationMethod(operation: CrudOperation) {
    switch (operation) {
      case 'edit':
        this.editPost();
        break;
      case 'delete':
        this.deletePost();
        break;
      case 'publish':
        this.publishPost();
        break;
      case 'unpublish':
        this.unpublishPost();
    }
  }

  private getDialogMessage(operation: CrudOperation) {
    switch (operation) {
      case 'edit':
        return msg.WANT_EDIT_POST_MSG;
      case 'delete':
        return msg.WANT_DELETE_POST_MSG;
      case 'publish':
        return msg.WANT_PUBLISH_POST_MSG;
      case 'unpublish':
        return msg.WANT_UNPUBLISH_POST_MSG;
    }
  }

  private editPost() {
    this.router.navigate([`/posts/${this.post.slug}/edit`]);
  }

  private deletePost() {
    this.postsService.delete(this.post.id).subscribe({
      next: () => {
        showSnackBar(this._snackBar, msg.SUCCESS_DELETE_POST_MSG, 'success');
        this.router.navigate(['/']);
      },
      error: (error) => showSnackBar(this._snackBar, error.message, 'error'),
    });
  }

  private publishPost() {
    this.postsService.publish(this.post.id).subscribe({
      next: () => {
        showSnackBar(this._snackBar, SUCCESS_PUBLISH_POST_MSG, 'success');
        this.router.navigate(['/']);
      },
      error: (error) => showSnackBar(this._snackBar, error.message, 'error'),
    });
  }

  private unpublishPost() {
    this.postsService.unpublish(this.post.id).subscribe({
      next: () => {
        showSnackBar(this._snackBar, msg.SUCCESS_UNPUBLISH_POST_MSG, 'success');
        this.router.navigate(['/']);
      },
      error: (error) => showSnackBar(this._snackBar, error.message, 'error'),
    });
  }
}
