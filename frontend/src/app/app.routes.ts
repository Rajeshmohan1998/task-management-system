import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login/login';
import { TaskList } from './features/tasks/task-list/task-list';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {path:'tasks', component: TaskList, canActivate:[authGuard]}
];