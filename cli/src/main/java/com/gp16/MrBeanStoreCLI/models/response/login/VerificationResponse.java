package com.gp16.MrBeanStoreCLI.models.response.login;

public record VerificationResponse(
        String device_code,
        String user_code,
        String verification_uri,
        Integer expires_in,
        Integer interval
) {}
