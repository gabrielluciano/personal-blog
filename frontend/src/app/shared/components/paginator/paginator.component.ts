import { isPlatformBrowser } from '@angular/common';
import { Component, HostListener, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.scss'],
})
export class PaginatorComponent extends MatPaginator implements OnInit {
  readonly MOBILE_BREAKPOINT = 480;

  // eslint-disable-next-line
  platformId: any = inject(PLATFORM_ID);

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    this.setHidePageSize();
  }

  override ngOnInit(): void {
    this.setHidePageSize();
    super.ngOnInit();
  }

  setHidePageSize(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.hidePageSize = window.innerWidth <= this.MOBILE_BREAKPOINT;
    }
  }
}
