import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PostReponse } from 'src/app/models/post/postResponse';
import { PillComponent } from '../../../shared/components/pill/pill.component';
import { DateFormatPipe } from '../../../shared/pipes/date-format.pipe';

@Component({
  selector: 'app-post-list-item',
  templateUrl: './post-list-item.component.html',
  standalone: true,
  imports: [RouterLink, PillComponent, DateFormatPipe],
})
export class PostListItemComponent {
  @Input() post!: PostReponse;
}
