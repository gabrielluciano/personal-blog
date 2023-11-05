import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-pill',
  templateUrl: './pill.component.html',
})
export class PillComponent {
  @Input() path = '';
}
