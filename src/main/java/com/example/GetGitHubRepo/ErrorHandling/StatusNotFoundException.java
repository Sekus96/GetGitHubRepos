package com.example.GetGitHubRepo.ErrorHandling;

public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(String message) {
        super(message);
    }
}