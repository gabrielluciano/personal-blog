import { NgModule } from '@angular/core';

import { HeaderComponent } from './header/header.component';
import { PillComponent } from './pill/pill.component';
import { CustomDateComponent } from './custom-date/custom-date.component';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    HeaderComponent,
    PillComponent,
    CustomDateComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HeaderComponent,
    PillComponent,
    CustomDateComponent
  ],
  providers: []
})
export class SharedModule { }
