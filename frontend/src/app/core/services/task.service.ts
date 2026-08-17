import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE'; // TODO: match your actual TaskStatus enum values exactly
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH'; // TODO: match your actual Priority enum values exactly

export interface Task {
  id: number;
  title: string;
  description: string;
  status: TaskStatus;
  priority: Priority;
  dueDate: string; // JSON dates arrive as strings — use `new Date(task.dueDate)` when needed
  assigneeUsername: string;
  createdByUsername: string;
}

export interface TaskRequest {
  title: string;
  description: string;
  status: TaskStatus;
  priority: Priority;
  dueDate: string; // send as ISO date string, e.g. '2026-08-20'
  assigneeUsername: string;
}

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // current page index
  size: number;
}

@Injectable({ providedIn: 'root' })
export class TaskService {

  private readonly apiUrl = 'http://localhost:8082/api/tasks';

  constructor(private http: HttpClient) {}

  // GET /api/tasks/my
  getMyTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/my`);
  }

  // GET /api/tasks (admin only, paginated, filterable)
  getAllTasks(status?: string, priority?: string, page = 0, size = 10): Observable<Page<Task>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (status) {
      params = params.set('status', status);
    }
    if (priority) {
      params = params.set('priority', priority);
    }

    return this.http.get<Page<Task>>(this.apiUrl, { params });
  }

  // POST /api/tasks
  createTask(request: TaskRequest): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, request);
  }

  // PUT /api/tasks/{id}
  updateTask(id: number, request: TaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, request);
  }

  // NOTE: no DELETE endpoint on backend yet — uncomment once TaskController adds @DeleteMapping
  // deleteTask(id: number): Observable<void> {
  //   return this.http.delete<void>(`${this.apiUrl}/${id}`);
  // }

  // NOTE: no GET /{id} endpoint yet either — add if you need a single-task detail view
  // getTaskById(id: number): Observable<Task> {
  //   return this.http.get<Task>(`${this.apiUrl}/${id}`);
  // }
}