import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-github-login',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './github-login.component.html',
  styleUrls: ['./github-login.component.css']
})
export class GithubLoginComponent {

  // GitHub OAuth2 login function
  loginWithGithub(): void {
    // GitHub OAuth2 authorization URL
    // In a real application, you would use environment variables for client_id
    const githubAuthUrl = 'https://github.com/login/oauth/authorize';
    const clientId = 'Ov23lir0sDkWwoQ5FmEO'; // Replace with your actual GitHub client ID
    const redirectUri = `${window.location.origin}/callback`;
    const scope = 'user:email';

    // Construct the full authorization URL
    const authUrl = `${githubAuthUrl}?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}`;

    // Redirect to GitHub authorization page
    window.location.href = authUrl;
  }
}
