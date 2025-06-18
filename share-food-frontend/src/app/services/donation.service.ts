import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../config/api-config';

@Injectable({
  providedIn: 'root'
})
export class DonationService {
  constructor(private http: HttpClient) { }

  submitDonationRequest(donationRequest: any): Observable<any> {
    return this.http.post(API_ENDPOINTS.DONATION.CREATE, donationRequest);
  }

  getDonationRequests(): Observable<any[]> {
    return this.http.get<any[]>(API_ENDPOINTS.DONATION.LIST, { withCredentials: true });
  }

  getDonationRequestById(id: number): Observable<any> {
    return this.http.get<any>(`${API_ENDPOINTS.DONATION.GET_BY_ID}/${id}`, { withCredentials: true });
  }
}
