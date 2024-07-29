package com.example.GetGitHubRepo.Services;

import com.example.GetGitHubRepo.ErrorHandling.StatusNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class GetGitHubServiceImpl implements GetGitHubService {

    private final WebClient webClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public GetGitHubServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<ResponseEntity<?>> getRepositories(String username) {
        return webClientBuilder.build()
                .get()
                .uri("https://api.github.com/users/{username}/repos", username)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> {
                    return Mono.error(new StatusNotFoundException("User " + username + " does not exist."));
                })
                .bodyToMono(String.class)
                .flatMap(response -> getResponseRepositories(username, response));
    }

    private Mono<ResponseEntity<?>> getResponseRepositories(String username, String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);


            List<JsonNode> nonForkRepos = getNonForkRepos(rootNode);

            JsonNode firstRepo = rootNode.get(0);
            JsonNode login = firstRepo.path("owner").path("login");

            List<Mono<Map<String, Object>>> fullList = getFullList(nonForkRepos);

            return Flux.merge(fullList).collectList()
                    .map(fullInfo -> ResponseEntity.ok().body(fullInfo));

        } catch (Exception e) {
            e.printStackTrace();
            throw new StatusNotFoundException("User " + username + " does not exist.");
        }
    }

    private List<Mono<Map<String, Object>>> getFullList(List<JsonNode> nonForkRepos) {
        List<Mono<Map<String, Object>>> fullList = new ArrayList<>();
        nonForkRepos.forEach(repo -> {
            String repoName = repo.path("name").asText();
            String repoOwner = repo.path("owner").path("login").asText();

            Mono<Map<String, Object>> branchList = getBranchList(repoName, repoOwner);
            fullList.add(branchList);
        });
        return fullList;
    }

    private Mono<Map<String, Object>> getBranchList(String repoName, String repoOwner) {
        Mono<Map<String, Object>> branchList = webClientBuilder.build()
                .get()
                .uri("https://api.github.com/repos/{owner}/{repo}/branches", repoOwner, repoName)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(branches -> {
                    List<Mono<Map<String, String>>> branchCommitList = getBranchCommitList(repoName, repoOwner, branches);
                    return Flux.merge(branchCommitList).collectList()
                            .map(branchInfos -> {
                                Map<String, Object> repoInfo = new LinkedHashMap<>();
                                repoInfo.put("repoName", repoName);
                                repoInfo.put("ownerLogin", repoOwner);
                                repoInfo.put("branches", branchInfos);
                                return repoInfo;
                            });
                });
        return branchList;
    }

    private static List<JsonNode> getNonForkRepos(JsonNode rootNode) {
        List<JsonNode> nonForkRepos = new ArrayList<>();
        rootNode.forEach(repo -> {
            boolean isFork = repo.path("fork").asBoolean(false);
            if (!isFork) {
                nonForkRepos.add(repo);
            }
        });
        return nonForkRepos;
    }

    private List<Mono<Map<String, String>>> getBranchCommitList(String repoName, String repoOwner, JsonNode branches) {
        List<Mono<Map<String, String>>> branchCommitList = new ArrayList<>();
        branches.forEach(branch -> {
            String branchName = branch.path("name").asText();
            Mono<Map<String, String>> branchCommitString = webClientBuilder.build()
                    .get()
                    .uri("https://api.github.com/repos/{owner}/{repo}/commits?sha={branch}", repoOwner, repoName, branchName)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(commitsJson -> {
                        JsonNode latestCommit = commitsJson.get(0);
                        String commitSha = latestCommit.path("sha").asText();
                        Map<String, String> branchInfo = new HashMap<>();
                        branchInfo.put("branchName", branchName);
                        branchInfo.put("latestCommitSha", commitSha);
                        return branchInfo;
                    });
            branchCommitList.add(branchCommitString);
        });
        return branchCommitList;
    }
}
