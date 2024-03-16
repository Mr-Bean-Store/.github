package com.gp16.MrBeanStoreCLI.models.response.login;

public record EmailResponse(String email, boolean verified, boolean primary, String visibility) {}