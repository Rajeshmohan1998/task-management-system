import { Component, OnInit, signal } from '@angular/core';
import { Task, TaskRequest, TaskService } from '../../../core/services/task.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-list',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss',
})
export class TaskList implements OnInit {


    tasks = signal<Task[]>([]);
  loading = signal(true);
  errorMessage = signal('');
  errorMessageN='';

  showForm = false;
  editingTask: Task | null = null;

  taskForm: FormGroup ;

  constructor(private fb: FormBuilder, private taskService: TaskService) {

    this.taskForm = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    status: ['TODO', Validators.required],
    priority: ['MEDIUM', Validators.required],
    dueDate: [''],
    assigneeUsername: [''],
  });


  }

  ngOnInit(): void {
    this.loadTasks();
  }

 loadTasks(): void {
    this.loading.set(true);
    this.taskService.getMyTasks().subscribe({
      next: (tasks) => {
        this.tasks.set(tasks);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Could not load tasks.');
        this.loading.set(false);
      },
    });
  }


  openCreateForm(): void {
    this.editingTask = null;
    this.taskForm.reset({ status: 'TODO', priority: 'MEDIUM' });
    this.showForm = true;
  }

  openEditForm(task: Task): void {
    this.editingTask = task;
    this.taskForm.patchValue(task);
    this.showForm = true;
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      return;
    }

    const request: TaskRequest = this.taskForm.value;

    const action = this.editingTask
      ? this.taskService.updateTask(this.editingTask.id, request)
      : this.taskService.createTask(request);

    action.subscribe({
      next: () => {
        this.showForm = false;
        this.loadTasks();
      },
      error: () => {
        this.errorMessageN = 'Could not save task.';
      },
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingTask = null;
  }
}
