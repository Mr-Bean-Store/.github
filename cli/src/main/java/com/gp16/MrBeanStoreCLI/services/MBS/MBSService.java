package com.gp16.MrBeanStoreCLI.services.MBS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.api.MBSHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderObject;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.OrderPost;
import com.gp16.MrBeanStoreCLI.models.response.MBS.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MBSService {
    MBSHttpAPIHandler mbsHttpAPIHandler;

    public MBSService(MBSHttpAPIHandler mbsHttpAPIHandler) {
        this.mbsHttpAPIHandler = mbsHttpAPIHandler;
    }

    public CustomerResponse isCustomerRegistered(String email) throws JsonProcessingException {
        return mbsHttpAPIHandler.isCustomerRegistered(email);
    }

    public CustomerResponse registerCustomer(String firstname, String lastname, String email) throws JsonProcessingException {
        return mbsHttpAPIHandler.registerCustomer(firstname, lastname, email);
    }

    public List<ProductItem> getProducts() {
        return  mbsHttpAPIHandler.getProducts();
    }

    public AddedOrderResponse addToOrder(OrderObject orderObject) throws  JsonProcessingException {
        return mbsHttpAPIHandler.addToOrder(orderObject);
    }

    public MBSAddressResponse addAddress(MappedAddressResponse mappedAddressResponse) throws JsonProcessingException{
        return mbsHttpAPIHandler.addAddress(mappedAddressResponse);
    }

    public List<HashMap<String, List<ProductItem>>> customerOrders(Long customer_id) throws JsonProcessingException{
        return mbsHttpAPIHandler.customerOrders(customer_id);
    }

    public AddedOrderResponse getOrder(Long orderId) {
        return mbsHttpAPIHandler.getOrder(orderId);
    }

    public PriceResponse orderPrice(Long orderId) {
        return  mbsHttpAPIHandler.orderPrice(orderId);
    }
}