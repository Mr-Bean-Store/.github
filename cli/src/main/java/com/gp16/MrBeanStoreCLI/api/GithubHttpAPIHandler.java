package com.gp16.MrBeanStoreCLI.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GithubHttpAPIHandler {
    RestClient restClient;

    public GithubHttpAPIHandler() {
        restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new RestClientException(response.toString());
                        })
                )
                .build();
    }

    public UserResponse getUser(String access_token) {
        return  restClient.get()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .body(UserResponse.class);

    }

    public EmailResponse getEmail(String access_token) {
        List<EmailResponse> response = restClient.get()
                .uri("/user/emails")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assert response != null;
        List<EmailResponse> filteredResponse = response.stream().filter(emailObject -> emailObject.primary()).toList();

        return filteredResponse.getFirst();
    }
}
