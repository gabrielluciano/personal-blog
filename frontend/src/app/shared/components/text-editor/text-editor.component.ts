import { AfterViewInit, Component, EventEmitter, Inject, Output, PLATFORM_ID } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import type EasyMDE from 'easymde';
import { ImageModalComponent, ImageModalResult } from '../image-modal/image-modal.component';
import { environment as env } from 'src/environments/environment';

const iconClassMap = {
  'bold': 'fa fa-bold',
  'italic': 'fa fa-italic',
  'strikethrough': 'fa fa-strikethrough',
  'heading': 'fa fa-header fa-heading',
  'heading-smaller': 'fa fa-header fa-heading header-smaller',
  'heading-bigger': 'fa fa-header fa-heading header-bigger',
  'heading-1': 'fa fa-header fa-heading header-1',
  'heading-2': 'fa fa-header fa-heading header-2',
  'heading-3': 'fa fa-header fa-heading header-3',
  'code': 'fa fa-code',
  'quote': 'fa fa-quote-left',
  'ordered-list': 'fa fa-list-ol',
  'unordered-list': 'fa fa-list-ul',
  'clean-block': 'fa fa-eraser',
  'link': 'fa fa-link',
  'image': 'fa fa-image',
  'upload-image': 'fa fa-image',
  'table': 'fa fa-table',
  'horizontal-rule': 'fa fa-minus',
  'preview': 'fa fa-eye',
  'side-by-side': 'fa fa-columns',
  'fullscreen': 'fa fa-arrows-alt',
  'guide': 'fa fa-question-circle',
  'undo': 'fa fa-undo',
  'redo': 'fa fa-repeat fa-redo',
};

@Component({
  selector: 'app-text-editor',
  standalone: true,
  imports: [],
  templateUrl: './text-editor.component.html',
  styleUrl: './text-editor.component.scss',
})
export class TextEditorComponent implements AfterViewInit {
  @Output() contentChange: EventEmitter<string> = new EventEmitter<string>();
  private easyMDE!: EasyMDE;

  constructor(
    @Inject(PLATFORM_ID) private platformId: object,
    private dialog: MatDialog,
  ) {}

  ngAfterViewInit(): void {
    this.initEasyMDE();
  }

  setContent(content: string) {
    if (this.easyMDE) {
      this.easyMDE.value(content);
    }
  }

  private async initEasyMDE() {
    const m = (await import('easymde')).default as typeof EasyMDE;

    this.easyMDE = new m({
      element: document.getElementById('markdown-editor')!,
      toolbar: [
        {
          name: 'image',
          action: this.uploadAndInsertImage.bind(this),
          className: 'fa fa-picture-o',
          title: 'Upload and Insert Image with srcset',
        },
        {
          name: 'bold',
          action: m.toggleBold,
          className: iconClassMap['bold'],
          title: 'Bold',
        },
        {
          name: 'italic',
          action: m.toggleItalic,
          className: iconClassMap['italic'],
          title: 'Italic',
        },
        {
          name: 'strikethrough',
          action: m.toggleStrikethrough,
          className: iconClassMap['strikethrough'],
          title: 'Strikethrough',
        },
        {
          name: 'heading',
          action: m.toggleHeadingSmaller,
          className: iconClassMap['heading'],
          title: 'Heading',
        },
        {
          name: 'heading-smaller',
          action: m.toggleHeadingSmaller,
          className: iconClassMap['heading-smaller'],
          title: 'Smaller Heading',
        },
        {
          name: 'heading-bigger',
          action: m.toggleHeadingBigger,
          className: iconClassMap['heading-bigger'],
          title: 'Bigger Heading',
        },
        {
          name: 'heading-1',
          action: m.toggleHeading1,
          className: iconClassMap['heading-1'],
          title: 'Big Heading',
        },
        {
          name: 'heading-2',
          action: m.toggleHeading2,
          className: iconClassMap['heading-2'],
          title: 'Medium Heading',
        },
        {
          name: 'heading-3',
          action: m.toggleHeading3,
          className: iconClassMap['heading-3'],
          title: 'Small Heading',
        },

        {
          name: 'code',
          action: m.toggleCodeBlock,
          className: iconClassMap['code'],
          title: 'Code',
        },
        {
          name: 'quote',
          action: m.toggleBlockquote,
          className: iconClassMap['quote'],
          title: 'Quote',
        },
        {
          name: 'unordered-list',
          action: m.toggleUnorderedList,
          className: iconClassMap['unordered-list'],
          title: 'Generic List',
        },
        {
          name: 'ordered-list',
          action: m.toggleOrderedList,
          className: iconClassMap['ordered-list'],
          title: 'Numbered List',
        },
        {
          name: 'clean-block',
          action: m.cleanBlock,
          className: iconClassMap['clean-block'],
          title: 'Clean block',
        },

        {
          name: 'link',
          action: m.drawLink,
          className: iconClassMap['link'],
          title: 'Create Link',
        },
        {
          name: 'image',
          action: m.drawImage,
          className: iconClassMap['image'],
          title: 'Insert Image',
        },
        {
          name: 'upload-image',
          action: m.drawUploadedImage,
          className: iconClassMap['upload-image'],
          title: 'Import an image',
        },
        {
          name: 'table',
          action: m.drawTable,
          className: iconClassMap['table'],
          title: 'Insert Table',
        },
        {
          name: 'horizontal-rule',
          action: m.drawHorizontalRule,
          className: iconClassMap['horizontal-rule'],
          title: 'Insert Horizontal Line',
        },

        {
          name: 'preview',
          action: m.togglePreview,
          className: iconClassMap['preview'],
          noDisable: true,
          title: 'Toggle Preview',
        },
        {
          name: 'side-by-side',
          action: m.toggleSideBySide,
          className: iconClassMap['side-by-side'],
          noDisable: true,
          noMobile: true,
          title: 'Toggle Side by Side',
        },
        {
          name: 'fullscreen',
          action: m.toggleFullScreen,
          className: iconClassMap['fullscreen'],
          noDisable: true,
          noMobile: true,
          title: 'Toggle Fullscreen',
        },

        {
          name: 'undo',
          action: m.undo,
          className: iconClassMap['undo'],
          noDisable: true,
          title: 'Undo',
        },
        {
          name: 'redo',
          action: m.redo,
          className: iconClassMap['redo'],
          noDisable: true,
          title: 'Redo',
        },
      ],
    });

    this.easyMDE.codemirror.on('change', () => {
      const content = this.easyMDE.value();
      this.contentChange.emit(content);
    });
  }

  private uploadAndInsertImage() {
    const dialogRef = this.dialog.open(ImageModalComponent, {
      panelClass: 'dialog',
    });

    dialogRef.afterClosed().subscribe((result: ImageModalResult) => {
      if (!result.isCancel && result.metadata) {
        const imageUrl1024 = this.generateImageUrl(result.metadata.image.variants[1024]);
        const imageUrl768 = this.generateImageUrl(result.metadata.image.variants[768]);
        const imageUrl480 = this.generateImageUrl(result.metadata.image.variants[480]);
        const imageUrl300 = this.generateImageUrl(result.metadata.image.variants[300]);

        const figure =
          `<figure>` +
          `<img alt="${result.metadata.alt}"` +
          ` title="${result.metadata.title}"` +
          ` src="${imageUrl1024}"` +
          ` srcset="` +
          `${imageUrl1024} 1024w, ` +
          `${imageUrl768} 768w, ` +
          `${imageUrl480} 480w, ` +
          `${imageUrl300} 300w"` +
          ` sizes="` +
          `(max-width: 300px) 270px, ` +
          `(max-width: 480px) 450px, ` +
          `(max-width: 768px) 740px, ` +
          `1024px"` +
          ` />` +
          `<figcaption>${result.metadata.caption}</figcaption>` +
          `</figure>`;

        this.easyMDE.codemirror.replaceSelection(figure);
      }
    });
  }

  private generateImageUrl(path: string) {
    return env.apiUrl + path.slice(1);
  }
}
