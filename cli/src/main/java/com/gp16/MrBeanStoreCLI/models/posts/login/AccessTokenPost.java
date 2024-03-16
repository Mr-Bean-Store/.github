package com.gp16.MrBeanStoreCLI.models.posts.login;

public record AccessTokenPost(String client_id, String device_code, String grant_type) {}
