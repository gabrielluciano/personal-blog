import { NgModule } from '@angular/core';

import { HeaderComponent } from './components/header/header.component';
import { PillComponent } from './components/pill/pill.component';
import { CustomDateComponent } from './components/custom-date/custom-date.component';
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
