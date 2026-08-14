package com.rajesh.task_service.controller;

import com.rajesh.task_service.dto.TaskRequest;
import com.rajesh.task_service.dto.TaskResponse;
import com.rajesh.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //---CREATE----
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse response = taskService.createTask((request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest request){

        TaskResponse response = taskService.updateTask(id,request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(){

        List<TaskResponse> tasks = taskService.getTasksForUser();
        return ResponseEntity.ok(tasks);

    }

    //---Get all tasks - ADMIN ONLY, with filter + pagination
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Pageable pageable){
        Page<TaskResponse> tasks = taskService.getAllTasks(status,priority,pageable);
        return ResponseEntity.ok(tasks);
    }
}
