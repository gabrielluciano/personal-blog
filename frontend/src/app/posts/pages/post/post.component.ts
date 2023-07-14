import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { PostReponse } from 'src/app/models/post/postResponse';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';
import {
  SnackbarComponent,
  SnackbarData,
  getSnackBarDefaultConfig,
} from 'src/app/shared/components/snackbar/snackbar.component';
import { AuthService } from 'src/app/shared/services/auth.service';
import { PostsService } from 'src/app/shared/services/posts.service';

type CrudOperation = 'edit' | 'delete' | 'publish' | 'unpublish';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
})
export class PostComponent implements OnInit {
  post!: PostReponse;
  editor = false;

  constructor(
    private postsService: PostsService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.params['slug'];
    this.findBySlug(slug);
    this.setEditor();
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
      next: (post) => (this.post = post),
      error: (error) => {
        console.warn(error);
        this.router.navigate(['/']);
      },
    });
  }

  private async setEditor() {
    this.editor = await this.authService.isEditor();
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
        return 'Deseja editar o post?';
      case 'delete':
        return 'Deseja excluir o post?';
      case 'publish':
        return 'Deseja publicar o post?';
      case 'unpublish':
        return 'Deseja despublicar o post?';
    }
  }

  private editPost() {
    this.router.navigate([`/posts/${this.post.slug}/edit`]);
  }

  private deletePost() {
    this.postsService.delete(this.post.id).subscribe({
      next: () => {
        this.showSnackBar('Post excluído com sucesso', 'success');
        this.router.navigate(['/']);
      },
      error: (error) => this.showSnackBar(error.message, 'error'),
    });
  }

  private publishPost() {
    this.postsService.publish(this.post.id).subscribe({
      next: () => {
        this.showSnackBar('Post publicado com sucesso', 'success');
        this.router.navigate(['/']);
      },
      error: (error) => this.showSnackBar(error.message, 'error'),
    });
  }

  private unpublishPost() {
    this.postsService.unpublish(this.post.id).subscribe({
      next: () => {
        this.showSnackBar('Post despublicado com sucesso', 'success');
        this.router.navigate(['/']);
      },
      error: (error) => this.showSnackBar(error.message, 'error'),
    });
  }

  private showSnackBar(message: string, style: SnackbarData['style']) {
    this._snackBar.openFromComponent(SnackbarComponent, getSnackBarDefaultConfig(message, style));
  }
}
