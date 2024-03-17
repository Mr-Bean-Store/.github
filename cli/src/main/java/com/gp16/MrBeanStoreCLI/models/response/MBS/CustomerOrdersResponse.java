package com.gp16.MrBeanStoreCLI.models.response.MBS;

import java.util.HashMap;
import java.util.List;

public record CustomerOrdersResponse(
        List<HashMap<Long, List<ProductItem>>> order
) {}
