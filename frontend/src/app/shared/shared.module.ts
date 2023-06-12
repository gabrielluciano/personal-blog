import { NgModule } from '@angular/core';

import { HeaderComponent } from './components/header/header.component';
import { PillComponent } from './components/pill/pill.component';
import { CommonModule } from '@angular/common';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { RouterModule } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';
import { PaginatorComponent } from './components/paginator/paginator.component';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [
    HeaderComponent,
    PillComponent,
    DateFormatPipe,
    FooterComponent,
    PaginatorComponent,
  ],
  imports: [CommonModule, RouterModule, MatPaginatorModule],
  exports: [HeaderComponent, FooterComponent, PillComponent, PaginatorComponent, DateFormatPipe],
  providers: [],
})
export class SharedModule {}
