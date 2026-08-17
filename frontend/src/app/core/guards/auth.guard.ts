import {inject} from '@angular/core';
import { CanActivateFn, Router} from '@angular/router';

export const authGuard: CanActivateFn = (route,state)=>{

    const router = inject(Router);

    const token = localStorage.getItem('token');

    //1. No token at all -> not logged in, redirect to login

    if(!token){
        router.navigate(['/login']);
        return false;
    }

    //2. Check if the route requires a specific role (set via route config's `data property)
    const requiredRole = route.data['role'] as string | undefined;

    if(requiredRole){
        const userRole = gettRoleFromToken(token);

       if(userRole !== requiredRole){
        router.navigate(['/login']);
        return false;
       } 
    }

    //3. token exists, role matches -- allow navigation
    return true;

};

function gettRoleFromToken(token: string):string |null{
    try{
        const payload = token.split('.')[1];
        const decoded = JSON.parse(atob(payload));
        return decoded.role ?? null;
    }catch{
        return null;
    }
}