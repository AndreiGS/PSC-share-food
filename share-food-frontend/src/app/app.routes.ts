import { Routes } from '@angular/router';
import { GithubLoginComponent } from './github-login/github-login.component';
import { CallbackComponent } from './callback/callback.component';
import { DonationRequestComponent } from './donation-request/donation-request.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: GithubLoginComponent },
  { path: 'callback', component: CallbackComponent },
  { path: 'donation-request', component: DonationRequestComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
