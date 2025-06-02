import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { DonationService } from '../services/donation.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-donation-request',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './donation-request.component.html',
  standalone: true,
  styleUrl: './donation-request.component.css'
})
export class DonationRequestComponent implements OnInit {
  donationRequestForm!: FormGroup;
  submitting = false;
  submitSuccess = false;
  submitError: string | null = null;
  userData: any = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private donationService: DonationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.userData = this.authService.getAuthData();
  }

  logout(): void {
    this.authService.logout();
  }

  // Custom validator to ensure at least one donation type is selected
  atLeastOneDonationTypeValidator(control: AbstractControl): ValidationErrors | null {
    const food = control.get('donationTypeFood')?.value;
    const money = control.get('donationTypeMoney')?.value;

    return food || money ? null : { noDonationTypeSelected: true };
  }

  initForm(): void {
    this.donationRequestForm = this.fb.group({
      title: ['', [Validators.required]],
      organization: ['', [Validators.required]],
      periodStart: ['', [Validators.required]],
      periodEnd: ['', [Validators.required]],
      description: ['', [Validators.required]],
      donationTypeFood: [false],
      donationTypeMoney: [false]
    }, {
      validators: this.atLeastOneDonationTypeValidator
    });
  }

  onSubmit(): void {
    if (this.donationRequestForm.valid) {
      this.submitting = true;
      this.submitSuccess = false;
      this.submitError = null;

      // Get the form values
      const formValues = this.donationRequestForm.value;

      // Prepare the donation request data
      const donationRequest = {
        title: formValues.title,
        description: formValues.description,
        organization: formValues.organization,
        donationTypeFood: formValues.donationTypeFood,
        donationTypeMoney: formValues.donationTypeMoney,
        periodStart: formValues.periodStart,
        periodEnd: formValues.periodEnd
        // userId is set by the backend based on the authenticated user
      };

      // Submit the donation request
      this.donationService.submitDonationRequest(donationRequest).subscribe({
        next: (response) => {
          this.submitting = false;
          this.submitSuccess = true;

          // Reset the form after successful submission
          this.donationRequestForm.reset({
            donationTypeFood: false,
            donationTypeMoney: false
          });

          console.log('Donation request submitted successfully:', response);
        },
        error: (error) => {
          this.submitting = false;
          this.submitError = 'Failed to submit donation request. Please try again.';
          console.error('Error submitting donation request:', error);
        }
      });
    } else {
      // Mark all fields as touched to trigger validation messages
      Object.keys(this.donationRequestForm.controls).forEach(key => {
        const control = this.donationRequestForm.get(key);
        control?.markAsTouched();
      });
    }
  }
}
