import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { PostsRoutingModule } from './posts-routing.module';
import { PostsHomeComponent } from './pages/posts-home/posts-home.component';
import { HeroComponent } from '../shared/components/hero/hero.component';
import { PostListItemComponent } from './components/post-list-item/post-list-item.component';
import { PostListComponent } from './components/post-list/post-list.component';
import { SharedModule } from '../shared/shared.module';
import { PostsTagComponent } from './pages/posts-tag/posts-tag.component';

@NgModule({
  declarations: [
    PostsHomeComponent,
    HeroComponent,
    PostListItemComponent,
    PostListComponent,
    PostsTagComponent,
  ],
  imports: [CommonModule, MatProgressSpinnerModule, PostsRoutingModule, SharedModule],
})
export class PostsModule {}
