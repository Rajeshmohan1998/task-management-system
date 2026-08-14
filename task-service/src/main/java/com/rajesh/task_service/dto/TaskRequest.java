package com.rajesh.task_service.dto;

import com.rajesh.task_service.enums.Priority;
import com.rajesh.task_service.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Date dueDate;
    private String assigneeUsername;
}
