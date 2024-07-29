package com.example.GetGitHubRepo.dto;

import lombok.Data;

@Data
public class UsernameErrorResponse {

    private int status;
    private String message;

    public UsernameErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
