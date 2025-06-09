import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-github-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './github-login.component.html',
  styleUrls: ['./github-login.component.css']
})
export class GithubLoginComponent {

  loginWithGithub(): void {
    const githubAuthUrl = 'https://github.com/login/oauth/authorize';
    const clientId = 'Ov23lir0sDkWwoQ5FmEO';
    const redirectUri = `${window.location.origin}/callback`;
    const scope = 'user:email';

    // Redirect to GitHub authorization page
    window.location.href = `${githubAuthUrl}?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`;
  }
}
