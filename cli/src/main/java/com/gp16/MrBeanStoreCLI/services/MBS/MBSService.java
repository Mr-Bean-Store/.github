package com.gp16.MrBeanStoreCLI.services.MBS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.api.MBSHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderObject;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderPost;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MBSAddressResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MappedAddressResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.ProductItem;
import com.gp16.MrBeanStoreCLI.models.response.MBS.CustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MBSService {
    MBSHttpAPIHandler mbsHttpAPIHandler;

    public MBSService(MBSHttpAPIHandler mbsHttpAPIHandler) {
        this.mbsHttpAPIHandler = mbsHttpAPIHandler;
    }

    public CustomerResponse registerCustomer(String firstname, String lastname, String email) throws JsonProcessingException {
        return mbsHttpAPIHandler.registerCustomer(firstname, lastname, email);
    }

    public List<ProductItem> getProducts() {
        return  mbsHttpAPIHandler.getProducts();
    }

    public void addToOrder(OrderObject orderObject) throws  JsonProcessingException {
        mbsHttpAPIHandler.addToOrder(orderObject);
    }

    public MBSAddressResponse addAddress(MappedAddressResponse mappedAddressResponse) throws JsonProcessingException{
        return mbsHttpAPIHandler.addAddress(mappedAddressResponse);
    }
}