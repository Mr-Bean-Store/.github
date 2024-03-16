package com.gp16.MrBeanStoreCLI.models.response.login;

public record AccessTokenResponse(
        String access_token,
        String token_type,
        String scope,
        String error,
        String error_description,
        String error_uri

) {}
