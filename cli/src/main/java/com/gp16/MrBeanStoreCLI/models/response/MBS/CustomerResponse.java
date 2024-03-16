package com.gp16.MrBeanStoreCLI.models.response.MBS;

public record CustomerResponse(
        Long customer_id,
        String firstName,
        String lastName,
        String email
) {}
