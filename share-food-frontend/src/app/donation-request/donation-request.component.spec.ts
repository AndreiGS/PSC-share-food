import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { DonationRequestComponent } from './donation-request.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { DonationService } from '../services/donation.service';
import { AuthService } from '../services/auth.service';

describe('DonationRequestComponent', () => {
  let component: DonationRequestComponent;
  let fixture: ComponentFixture<DonationRequestComponent>;
  let donationServiceSpy: jasmine.SpyObj<DonationService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    donationServiceSpy = jasmine.createSpyObj('DonationService', ['submitDonationRequest']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getAuthData', 'logout']);

    // Mock user data
    authServiceSpy.getAuthData.and.returnValue({ name: 'Test User', id: 1 });

    await TestBed.configureTestingModule({
      imports: [
        DonationRequestComponent,
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        { provide: DonationService, useValue: donationServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DonationRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with default values', () => {
    expect(component.donationRequestForm).toBeDefined();
    expect(component.donationRequestForm.get('title')).toBeDefined();
    expect(component.donationRequestForm.get('organization')).toBeDefined();
    expect(component.donationRequestForm.get('periodStart')).toBeDefined();
    expect(component.donationRequestForm.get('periodEnd')).toBeDefined();
    expect(component.donationRequestForm.get('description')).toBeDefined();
    expect(component.donationRequestForm.get('donationTypeFood')).toBeDefined();
    expect(component.donationRequestForm.get('donationTypeMoney')).toBeDefined();
    expect(component.donationRequestForm.get('donationTypeFood')?.value).toBe(false);
    expect(component.donationRequestForm.get('donationTypeMoney')?.value).toBe(false);
  });

  it('should mark form as invalid when empty', () => {
    expect(component.donationRequestForm.valid).toBeFalsy();
  });

  it('should mark form as valid when all required fields are filled', () => {
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0];

    component.donationRequestForm.setValue({
      title: 'Test Title',
      organization: 'Test Organization',
      periodStart: today,
      periodEnd: tomorrow,
      description: 'Test Description',
      donationTypeFood: true,
      donationTypeMoney: false
    });

    expect(component.donationRequestForm.valid).toBeTruthy();
  });

  it('should submit form and reset on success', () => {
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0];

    // Fill the form
    component.donationRequestForm.setValue({
      title: 'Test Title',
      organization: 'Test Organization',
      periodStart: today,
      periodEnd: tomorrow,
      description: 'Test Description',
      donationTypeFood: true,
      donationTypeMoney: false
    });

    // Mock successful response
    donationServiceSpy.submitDonationRequest.and.returnValue(of({ id: 1 }));

    // Submit the form
    component.onSubmit();

    // Check if service was called with correct data
    expect(donationServiceSpy.submitDonationRequest).toHaveBeenCalledWith({
      title: 'Test Title',
      organization: 'Test Organization',
      periodStart: today,
      periodEnd: tomorrow,
      description: 'Test Description',
      donationTypeFood: true,
      donationTypeMoney: false
    });

    // Check if form was reset and success message shown
    expect(component.submitSuccess).toBe(true);
    expect(component.submitting).toBe(false);
    expect(component.donationRequestForm.get('title')?.value).toBeFalsy();
    expect(component.donationRequestForm.get('donationTypeFood')?.value).toBe(false);
    expect(component.donationRequestForm.get('donationTypeMoney')?.value).toBe(false);
  });

  it('should handle submission error', () => {
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0];

    // Fill the form
    component.donationRequestForm.setValue({
      title: 'Test Title',
      organization: 'Test Organization',
      periodStart: today,
      periodEnd: tomorrow,
      description: 'Test Description',
      donationTypeFood: true,
      donationTypeMoney: false
    });

    // Mock error response
    donationServiceSpy.submitDonationRequest.and.returnValue(throwError(() => new Error('Test error')));

    // Submit the form
    component.onSubmit();

    // Check if error is handled
    expect(component.submitError).toBe('Failed to submit donation request. Please try again.');
    expect(component.submitting).toBe(false);
    expect(component.submitSuccess).toBe(false);
  });
});
