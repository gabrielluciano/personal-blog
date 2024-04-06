import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'posts', loadChildren: () => import('./posts/posts.routes').then((m) => m.postRoutes) },
  { path: '', pathMatch: 'full', redirectTo: 'posts' },
  { path: '**', redirectTo: 'posts' },
];
