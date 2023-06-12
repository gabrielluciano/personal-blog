import { NgModule } from '@angular/core';

import { HeaderComponent } from './components/header/header.component';
import { PillComponent } from './components/pill/pill.component';
import { CommonModule } from '@angular/common';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { RouterModule } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';

@NgModule({
  declarations: [HeaderComponent, PillComponent, DateFormatPipe, FooterComponent],
  imports: [CommonModule, RouterModule],
  exports: [HeaderComponent, FooterComponent, PillComponent, DateFormatPipe],
  providers: [],
})
export class SharedModule {}
