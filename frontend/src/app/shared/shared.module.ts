import { NgModule } from '@angular/core';

import { HeaderComponent } from './header/header.component';
import { PillComponent } from './pill/pill.component';

@NgModule({
  declarations: [
    HeaderComponent,
    PillComponent
  ],
  imports: [],
  exports: [
    HeaderComponent,
    PillComponent
  ],
  providers: []
})
export class SharedModule { }
