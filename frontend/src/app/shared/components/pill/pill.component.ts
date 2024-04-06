import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-pill',
  templateUrl: './pill.component.html',
  standalone: true,
  imports: [RouterLink],
})
export class PillComponent {
  @Input() path = '';
}
