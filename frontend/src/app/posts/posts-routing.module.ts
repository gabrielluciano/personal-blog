import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PostsHomeComponent } from './pages/posts-home/posts-home.component';
import { PostsTagComponent } from './pages/posts-tag/posts-tag.component';
import { PostComponent } from './pages/post/post.component';
import { PostFormComponent } from './pages/post-form/post-form.component';
import { editorGuard } from '../shared/guards/auth.guard';

const routes: Routes = [
  { path: '', component: PostsHomeComponent },
  { path: 'new', component: PostFormComponent, canActivate: [editorGuard], data: { edit: false } },
  { path: ':slug', component: PostComponent },
  {
    path: ':slug/edit',
    component: PostFormComponent,
    canActivate: [editorGuard],
    data: { edit: true },
  },
  { path: 'tag/:tagId/:tagSlug', component: PostsTagComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PostsRoutingModule {}
