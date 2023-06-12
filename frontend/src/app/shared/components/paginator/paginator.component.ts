import { Component, HostListener } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss'],
})
export class PaginatorComponent extends MatPaginator {
  readonly MOBILE_BREAKPOINT = 480;

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    this.hidePageSize = window.innerWidth <= this.MOBILE_BREAKPOINT;
  }
}
