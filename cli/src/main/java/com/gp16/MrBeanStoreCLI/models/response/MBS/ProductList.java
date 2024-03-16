package com.gp16.MrBeanStoreCLI.models.response.MBS;

import com.gp16.MrBeanStoreCLI.models.response.MBS.ProductItem;

import java.util.List;

public record ProductList(
        List<ProductItem> productList
) {
}
