import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authReq = request.clone({
    withCredentials: true
  });
  return next(authReq);
};
