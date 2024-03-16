package com.gp16.MrBeanStoreCLI.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp16.MrBeanStoreCLI.models.posts.login.AccessTokenPost;
import com.gp16.MrBeanStoreCLI.models.posts.login.VerificationPost;
import com.gp16.MrBeanStoreCLI.models.response.login.AccessTokenResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.VerificationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class LoginHttpAPIHandler {
    RestClient restClient;
    ObjectMapper objectMapper = new ObjectMapper();

    public LoginHttpAPIHandler() {
        restClient = RestClient.builder()
                .baseUrl("https://github.com")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new RestClientException(response.toString());
                        })
                )
                .build();
    }

    public VerificationResponse requestVerificationCodes(String client_id, String scope) throws JsonProcessingException {
        VerificationPost post = new VerificationPost(client_id, scope);

        return restClient.post()
                .uri("/login/device/code")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(post))
                .retrieve()
                .body(VerificationResponse.class);
    } 

    public AccessTokenResponse requestAccessToken(String client_id, String device_code, String grant_type) throws JsonProcessingException {
        AccessTokenPost post = new AccessTokenPost(client_id, device_code, grant_type);

        return restClient.post()
                .uri("/login/oauth/access_token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(post))
                .retrieve()
                .body(AccessTokenResponse.class);
    }

    public UserResponse getUser(String access_token) {
        return restClient.get()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .body(UserResponse.class);
    }

    public EmailResponse getEmail(String access_token) {
        return restClient.get()
                .uri("/user/emails")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .body(EmailResponse.class);
    }
}
