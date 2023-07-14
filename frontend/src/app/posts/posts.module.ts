import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';

import { PostsRoutingModule } from './posts-routing.module';
import { PostsHomeComponent } from './pages/posts-home/posts-home.component';
import { HeroComponent } from '../shared/components/hero/hero.component';
import { PostListItemComponent } from './components/post-list-item/post-list-item.component';
import { PostListComponent } from './components/post-list/post-list.component';
import { SharedModule } from '../shared/shared.module';
import { PostsTagComponent } from './pages/posts-tag/posts-tag.component';
import { PostComponent } from './pages/post/post.component';
import { PostFormComponent } from './pages/post-form/post-form.component';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
  declarations: [
    PostsHomeComponent,
    HeroComponent,
    PostListItemComponent,
    PostListComponent,
    PostsTagComponent,
    PostComponent,
    PostFormComponent,
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    PostsRoutingModule,
    SharedModule,
    NgOptimizedImage,
  ],
})
export class PostsModule {}
