package com.rajesh.task_service.service;


import com.rajesh.task_service.client.AuthServiceClient;
import com.rajesh.task_service.dto.TaskRequest;
import com.rajesh.task_service.dto.TaskResponse;
import com.rajesh.task_service.entity.Task;
import com.rajesh.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuthServiceClient authServiceClient;


    // ---- CREATE ----
    public TaskResponse createTask(TaskRequest request){

        String currentUsername = getCurrentUsername();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setAssigneeUsername(request.getAssigneeUsername());
        task.setCreatedByUsername(currentUsername);

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    // ---- UPDATE ----
    public TaskResponse updateTask(Long id, TaskRequest request){
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task not found"));

        //Business rule: only the creator or admin , handled seperatedly can update this
        String currentUsername = getCurrentUsername();
        if(!task.getCreatedByUsername().equals(currentUsername)){
            throw new RuntimeException("Not authorized to update this task");
        }
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setAssigneeUsername(request.getAssigneeUsername());

        Task updated = taskRepository.save(task);
        return mapToResponse(updated);

    }

    // ---- Delete ----

    public void deleteTask(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException(("Task not found")));

        String currentUsername = getCurrentUsername();
        if(!task.getCreatedByUsername().equals(currentUsername)){
            throw new RuntimeException("Not authorized to delete this task");
        }
        taskRepository.deleteById(id);
    }

    // ---- Get TASKS FOR CURRENT USER ----
    public List<TaskResponse> getTasksForUser(){
        String currentUsername = getCurrentUsername();
        return taskRepository.findByAssigneeUsername(currentUsername)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---- GET ALL TASKS — ADMIN ONLY ----
    // Notice: no manual role-check code here. @PreAuthorize handles authorization
    // BEFORE this method body ever runs — SRP: this method's only job is "fetch all tasks."
    @PreAuthorize("hasRole('Admin')")
    public Page<TaskResponse> getAllTasks(String status, String priority, Pageable pageable){
        Page<Task> taskPage;
        if(status !=null && priority != null){
            taskPage = taskRepository.findByStatusAndPriority(status,priority,pageable);
        }else if(status !=null){
            taskPage = taskRepository.findByStatus(status,pageable);
        }else if (priority != null){
            taskPage = taskRepository.findByPriority(priority,pageable);
        }else{
            taskPage = taskRepository.findAll(pageable);
        }
        return taskPage
                .map(this::mapToResponse);
    }

    // ---- HELPER:Current authenticated username ----

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // ---- HELPER:entity -> response DTO ----
    private  TaskResponse mapToResponse(Task task){
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setAssigneeUsername(task.getAssigneeUsername());
        response.setCreatedByUsername(task.getCreatedByUsername());
        return response;
    }

}
