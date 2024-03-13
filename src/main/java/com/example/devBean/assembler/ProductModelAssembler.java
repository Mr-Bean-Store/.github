package com.example.devBean.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.devBean.controller.ProductController;
import com.example.devBean.model.Product;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(product,
            linkTo(methodOn(ProductController.class).oneProduct(product.getProductId())).withSelfRel(),
            linkTo(methodOn(ProductController.class).allProducts()).withRel("products"));
    }

}
