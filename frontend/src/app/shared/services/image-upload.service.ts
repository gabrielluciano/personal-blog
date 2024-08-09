import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { ImageUploadResponse } from 'src/app/models/image/imageUploadResponse';
import { environment as env } from 'src/environments/environment';
import { handleError } from '../util/errorHandling';

@Injectable({
  providedIn: 'root',
})
export class ImageUploadService {
  constructor(private http: HttpClient) {}

  uploadImage(file: File, name: string): Observable<HttpEvent<ImageUploadResponse>> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('file', file);

    const req = new HttpRequest('POST', env.apiUrl + 'image/upload', formData, {
      reportProgress: true,
    });

    return this.http.request<ImageUploadResponse>(req).pipe(catchError(handleError));
  }
}
