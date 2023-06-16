import { Component, HostListener, OnInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss'],
})
export class PaginatorComponent extends MatPaginator implements OnInit {
  readonly MOBILE_BREAKPOINT = 480;

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    this.setHidePageSize();
  }

  override ngOnInit(): void {
    this.setHidePageSize();
    super.ngOnInit();
  }

  setHidePageSize(): void {
    this.hidePageSize = window.innerWidth <= this.MOBILE_BREAKPOINT;
  }
}
