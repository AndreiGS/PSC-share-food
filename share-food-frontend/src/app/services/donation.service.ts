import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DonationService {
  private apiUrl = 'http://localhost:8080/share-food/api/v1/donation-requests';

  constructor(private http: HttpClient) { }

  submitDonationRequest(donationRequest: any): Observable<any> {
    return this.http.post(this.apiUrl, donationRequest);
  }

  getDonationRequests(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, { withCredentials: true });
  }

  getDonationRequestById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  updateDonationRequest(id: number, donationRequest: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, donationRequest, { withCredentials: true });
  }

  deleteDonationRequest(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
