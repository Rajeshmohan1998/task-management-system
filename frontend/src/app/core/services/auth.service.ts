
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";

interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    username: string;
    role: string;
}

interface RegisterRequest {
    username:string;
    password: string;
}

interface UserResponse {
    username: string;
    role: string;
    email: string
}

@Injectable({providedIn: 'root'})
export class AuthService{

    private readonly authUrl = 'http://localhost:8081/api/auth';
    private readonly usersUrl = 'http://localhost:8081/api/Users';


    constructor(private http: HttpClient){}

        login(username:String,password: string): Observable<LoginResponse>{
            return this.http.post<LoginResponse>(`${this.authUrl}/login`, {username,password})
            .pipe(
                tap(response => {
                    localStorage.setItem('token',response?.accessToken);
                    localStorage.setItem('refreshToken',  response?.refreshToken);
                    localStorage.setItem('role',response.role);
                })
            );
        }

        register(request:RegisterRequest):Observable<UserResponse> {
            return this.http.post<UserResponse>(`${this.usersUrl}/register`,request);
        }   
        
        logout():void{
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('role');
        }

        getToken(): string |null{
            return localStorage.getItem('token');
        }

        getRole():string |null {

            return localStorage.getItem('role');
        }

        isLoggedIn():boolean{
            return this.getToken()!==null;
        }

          refreshAccessToken(refreshToken: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.authUrl}/refresh`, { refreshToken });
  }
}