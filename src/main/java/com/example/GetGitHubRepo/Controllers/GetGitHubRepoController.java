package com.example.GetGitHubRepo.Controllers;

import com.example.GetGitHubRepo.ErrorHandling.StatusNotFoundException;
import com.example.GetGitHubRepo.Services.GetGitHubService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;


@RestController
@RequestMapping("/GetGitHubRepo/")
public class GetGitHubRepoController {

    private final GetGitHubService getGitHubService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public GetGitHubRepoController(GetGitHubService getGitHubService) {
        this.getGitHubService = getGitHubService;
    }

    @GetMapping("/repositories")
    public Mono<ResponseEntity<?>> getRepositories(@RequestParam String username) {
        return getGitHubService.getRepositories(username);
    }

}

