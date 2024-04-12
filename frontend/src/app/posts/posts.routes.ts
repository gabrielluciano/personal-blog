import { Routes } from '@angular/router';
import { editorGuard } from '../shared/guards/auth.guard';
import { PostFormComponent } from './pages/post-form/post-form.component';
import { PostComponent } from './pages/post/post.component';
import { PostsHomeComponent } from './pages/posts-home/posts-home.component';
import { PostsTagComponent } from './pages/posts-tag/posts-tag.component';

export const postRoutes: Routes = [
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
