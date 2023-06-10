import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-custom-date',
  templateUrl: './custom-date.component.html',
  styleUrls: ['./custom-date.component.scss'],
})
export class CustomDateComponent {
  @Input() dateString = '';
  @Input() class = '';
}
