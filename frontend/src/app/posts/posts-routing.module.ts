import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostsHomeComponent } from './pages/posts-home/posts-home.component';

const routes: Routes = [{ path: '', component: PostsHomeComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PostsRoutingModule { }
