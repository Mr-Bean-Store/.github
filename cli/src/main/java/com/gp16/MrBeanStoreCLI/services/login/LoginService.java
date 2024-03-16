package com.gp16.MrBeanStoreCLI.services.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.api.LoginHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.models.response.login.AccessTokenResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.EmailResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.UserResponse;
import com.gp16.MrBeanStoreCLI.models.response.login.VerificationResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    LoginHttpAPIHandler loginHttpAPIHandler;

    public LoginService(LoginHttpAPIHandler loginHttpAPIHandler) {
        this.loginHttpAPIHandler = loginHttpAPIHandler;
    }

    public VerificationResponse requestVerificationCodes(String client_id, String scope) throws JsonProcessingException {
        return loginHttpAPIHandler.requestVerificationCodes(client_id, scope);
    }

    public AccessTokenResponse requestAccessToken(String client_id, String device_code, String grant_type) throws JsonProcessingException {
        return loginHttpAPIHandler.requestAccessToken(client_id, device_code, grant_type);
    }

    public UserResponse getUser(String access_token) {
        return loginHttpAPIHandler.getUser(access_token);
    }

    public EmailResponse getEmail(String access_token) {
        return loginHttpAPIHandler.getEmail(access_token);
    }
}
