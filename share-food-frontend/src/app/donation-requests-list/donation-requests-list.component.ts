import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { DonationService } from '../services/donation.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-donation-requests-list',
  imports: [CommonModule],
  templateUrl: './donation-requests-list.component.html',
  standalone: true,
  styleUrl: './donation-requests-list.component.css'
})
export class DonationRequestsListComponent implements OnInit {
  donationRequests: any[] = [];
  loading = true;
  error: string | null = null;
  userData: any = null;

  constructor(
    private authService: AuthService,
    private donationService: DonationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userData = this.authService.getAuthData();
    this.loadDonationRequests();
  }

  loadDonationRequests(): void {
    this.loading = true;
    this.error = null;

    this.donationService.getDonationRequests().subscribe({
      next: (requests) => {
        this.donationRequests = requests;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading donation requests:', err);
        this.error = 'Failed to load donation requests. Please try again.';
        this.loading = false;
      }
    });
  }

  // Helper method to format donation type for display
  getDonationType(request: any): string {
    const types = [];
    if (request.donationTypeFood) types.push('Food');
    if (request.donationTypeMoney) types.push('Money');
    return types.join(' & ');
  }

}
