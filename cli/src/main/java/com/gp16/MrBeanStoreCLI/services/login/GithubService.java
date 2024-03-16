package com.gp16.MrBeanStoreCLI.services.login;

import com.gp16.MrBeanStoreCLI.api.GithubHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    GithubHttpAPIHandler githubHttpAPIHandler;

    public GithubService(GithubHttpAPIHandler githubHttpAPIHandler) {
        this.githubHttpAPIHandler = githubHttpAPIHandler;
    }

    public UserResponse getUser(String access_token) {
        return githubHttpAPIHandler.getUser(access_token);
    }

    public EmailResponse getEmail(String access_token) {
        return githubHttpAPIHandler.getEmail(access_token);
    }
}
