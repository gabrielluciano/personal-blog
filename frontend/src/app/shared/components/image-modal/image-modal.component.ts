import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ImageUploadResponse } from 'src/app/models/image/imageUploadResponse';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { ImageUploadService } from '../../services/image-upload.service';
import { HttpEventType } from '@angular/common/http';

interface ImageMetadata {
  alt: string;
  title: string;
  caption: string;
  image: ImageUploadResponse;
}

export interface ImageModalResult {
  isCancel: boolean;
  metadata?: ImageMetadata;
}

@Component({
  selector: 'app-image-modal',
  standalone: true,
  imports: [FormsModule, MatButton],
  templateUrl: './image-modal.component.html',
  styleUrl: './image-modal.component.scss',
})
export class ImageModalComponent {
  name = '';
  files: FileList | null = null;
  alt = '';
  title = '';
  caption = '';

  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private imageUploadService: ImageUploadService,
  ) {}

  onSubmit() {
    if (!this.files) return;
    const file = this.files[0];

    this.imageUploadService.uploadImage(file, this.name).subscribe({
      next: (event) => {
        if (event.type == HttpEventType.Response) {
          this.dialogRef.close({
            isCancel: false,
            metadata: {
              alt: this.alt,
              title: this.title,
              caption: this.caption,
              image: event.body,
            },
          } as ImageModalResult);
        }
      },
    });
  }

  onCancel() {
    this.dialogRef.close({ isCancel: true } as ImageModalResult);
  }

  onFileChange(event: Event) {
    if (event.target) {
      const el = event.target as HTMLInputElement;
      this.files = el.files;
    }
  }
}
