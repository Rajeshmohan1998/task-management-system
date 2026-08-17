package com.rajesh.task_service.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timeStamp;
    private int status;
    private String message;
    private String path;

}
