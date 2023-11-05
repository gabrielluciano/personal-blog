import { NgModule } from '@angular/core';

import { HeaderComponent } from './components/header/header.component';
import { PillComponent } from './components/pill/pill.component';
import { CommonModule } from '@angular/common';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { RouterModule } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';
import { PaginatorComponent } from './components/paginator/paginator.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SnackbarComponent } from './components/snackbar/snackbar.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MarkdownDirective } from './directives/markdown.directive';
import { MenuComponent } from './components/menu/menu.component';

@NgModule({
  declarations: [
    HeaderComponent,
    PillComponent,
    DateFormatPipe,
    FooterComponent,
    PaginatorComponent,
    LoginFormComponent,
    SnackbarComponent,
    ConfirmDialogComponent,
    MarkdownDirective,
    MenuComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatPaginatorModule,
    MatButtonModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSnackBarModule,
    MatIconModule,
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    PillComponent,
    PaginatorComponent,
    DateFormatPipe,
    MarkdownDirective,
  ],
  providers: [],
})
export class SharedModule {}
