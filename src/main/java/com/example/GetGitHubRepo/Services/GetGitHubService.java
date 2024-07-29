package com.example.GetGitHubRepo.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface GetGitHubService {
    public Mono<ResponseEntity<?>> getRepositories(String username);

}
