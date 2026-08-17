
import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { catchError, switchMap, throwError } from "rxjs"
import { AuthService } from "../services/auth.service";
import { inject } from "@angular/core";
import { Router } from "@angular/router";

export const authInterceptor: HttpInterceptorFn = (req,next)=>{

    const authService = inject(AuthService);
    const router = inject(Router);



    //1.Skip attaching a token for login/register - user has no token at this point
    if(req.url.includes('/auth/login')|| req.url.includes('/auth/register')){
        return next(req);
    }

    //2. Read the token from LocalStorage
    const token = localStorage.getItem('token');

    //3. If no token exists, just pass the request through unmodified
    if(!token){
        return next(req);
    }

    //4.Clone the request, adding the Authorization header
    const clonedrequest = req.clone({
        setHeaders:{
            Authorization:`Bearer ${token}`
        }
    });

    //5.pass the modified request along the chain
    return next(clonedrequest).pipe(
        catchError((error: HttpErrorResponse) => {

            const isAuthError = error.status === 401 || error.status === 403;
            const isRefreshCall = req.url.includes('/auth/refresh');

            if (!isAuthError || isRefreshCall) {
                return throwError(() => error);
            }

            const refreshToken = localStorage.getItem('refreshToken');
            if (!refreshToken) {
                return throwError(() => error);
            }

            return authService.refreshAccessToken(refreshToken).pipe(
                switchMap((response) => {
                    localStorage.setItem('token', response.accessToken);

                    const retriedRequest = req.clone({
                        setHeaders: {
                            Authorization: `Bearer ${response.accessToken}`
                        }
                    });
                    return next(retriedRequest);
                }),
                catchError((refreshError) => {
                    localStorage.removeItem('token');
                    localStorage.removeItem('refreshToken');
                    localStorage.removeItem('role');
                    router.navigate(['/login']); 
                    return throwError(() => refreshError);
                })
            );
        })
    );;


}