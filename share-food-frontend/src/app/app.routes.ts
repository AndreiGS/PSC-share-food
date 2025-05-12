import { Routes } from '@angular/router';
import { GithubLoginComponent } from './github-login/github-login.component';
import { CallbackComponent } from './callback/callback.component';

export const routes: Routes = [
  { path: '', component: GithubLoginComponent },
  { path: 'callback', component: CallbackComponent },
  { path: '**', redirectTo: '' }
];
