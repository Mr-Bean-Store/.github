package com.gp16.MrBeanStoreCLI.models.posts.MBS;

import java.util.List;

public record OrderObject(
        Long customer_id,
        Long address_id,
        List<Integer> product_ids
) {
}
