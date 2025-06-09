import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {
  code: string | null = null;
  loading: boolean = true;
  error: string | null = null;
  username: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Extract the code from the URL query parameters
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];

      if (this.code) {
        this.processOAuthCallback();
      } else {
        this.loading = false;
        this.error = 'No authorization code found in the URL';
      }
    });
  }

  processOAuthCallback(): void {
    if (!this.code) return;

    this.authService.processOAuthCallback('github', this.code).subscribe({
      next: () => {
        this.loading = false;
        this.username = this.authService.getUsername();

        // Navigate immediately without delay
        this.router.navigate(['/donation-request']);
      },
      error: (err) => {
        console.error('Error processing OAuth callback:', err);
        this.error = 'Authentication failed. Please try again.';
        this.loading = false;
      }
    });
  }
}
