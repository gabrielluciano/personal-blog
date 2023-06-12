import { NgModule } from '@angular/core';

import { HeaderComponent } from './components/header/header.component';
import { PillComponent } from './components/pill/pill.component';
import { CommonModule } from '@angular/common';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [HeaderComponent, PillComponent, DateFormatPipe],
  imports: [CommonModule, RouterModule],
  exports: [HeaderComponent, PillComponent, DateFormatPipe],
  providers: [],
})
export class SharedModule {}
