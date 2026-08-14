package com.rajesh.task_service.entity;

import com.rajesh.task_service.enums.Priority;
import com.rajesh.task_service.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length=50)
    private String title;

    @Column(columnDefinition ="TEXT" )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = true)
    private String assigneeUsername;

    @Column(nullable= true)
    private String createdByUsername;

}
