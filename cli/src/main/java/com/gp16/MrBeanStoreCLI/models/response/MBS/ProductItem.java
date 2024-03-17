package com.gp16.MrBeanStoreCLI.models.response.MBS;

import com.gp16.MrBeanStoreCLI.models.response.MBS.ModelResponse;

public record ProductItem(
        Long productId,
        String serialNumber,
        ModelResponse model
) {
}
