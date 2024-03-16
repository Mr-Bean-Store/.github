package com.gp16.MrBeanStoreCLI.models.response.MBS;

public record MBSAddressResponse(
        Long addressId,
        double latitude,
        double longitude
) {}
