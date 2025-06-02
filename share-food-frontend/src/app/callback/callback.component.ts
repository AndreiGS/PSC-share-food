import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
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
  responseData: any = null;
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Extract the code from the URL query parameters
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];

      if (this.code) {
        this.fetchUserData();
      } else {
        this.loading = false;
        this.error = 'No authorization code found in the URL';
      }
    });
  }

  fetchUserData(): void {
    // Make a GET request to the backend API with the authorization code
    const apiUrl = `http://localhost:8080/share-food/api/v1/oauth/callback?provider=github&code=${this.code}`;

    this.http.post(apiUrl, {}).subscribe({
      next: (response) => {
        this.responseData = response;
        this.loading = false;

        // Store authentication data
        this.authService.setAuthData(response);

        // Redirect to donation-request page
        setTimeout(() => {
          this.router.navigate(['/donation-request']);
        }, 1000); // Short delay to show the success message
      },
      error: (err) => {
        console.error('Error fetching user data:', err);
        this.error = 'Failed to fetch user data. Please try again.';
        this.loading = false;
      }
    });
  }

  // Helper method to format JSON for display
  formatJson(json: any): string {
    return JSON.stringify(json, null, 2);
  }
}
