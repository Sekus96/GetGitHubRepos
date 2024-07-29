package com.example.GetGitHubRepo.ErrorHandling;

import com.example.GetGitHubRepo.ErrorHandling.StatusNotFoundException;
import com.example.GetGitHubRepo.dto.UsernameErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UsernameErrorResponse> handleException(StatusNotFoundException e){
        UsernameErrorResponse error = new UsernameErrorResponse((HttpStatus.NOT_FOUND.value()), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
