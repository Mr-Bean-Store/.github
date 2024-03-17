package com.gp16.MrBeanStoreCLI.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.*;
import com.gp16.MrBeanStoreCLI.models.response.MBS.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class MBSHttpAPIHandler {
    RestClient restClient;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public MBSHttpAPIHandler() {
        restClient = RestClient.builder()
                .baseUrl("http://34.248.119.27:8090/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new RestClientException(response.toString());
                        })
                )
                .build();
    }

    public CustomerResponse isCustomerRegistered(String email) throws JsonProcessingException {
        Object response = restClient.get()
                .uri("/customer-by-email/" + email)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Object.class);

        String stringResponse = objectMapper.writeValueAsString(response);

        if (objectMapper.readValue(stringResponse, MessageResponse.class).message() != null) {
            return new CustomerResponse(0L, "", "", "");
        }

        return objectMapper.readValue(stringResponse, CustomerResponse.class);
    }

    public CustomerResponse registerCustomer(String firstname, String lastname, String email) throws JsonProcessingException {
        CustomerPost post = new CustomerPost(firstname, lastname, email);

        return restClient.post()
                .uri("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(post))
                .retrieve()
                .body(CustomerResponse.class);
    }

    public List<ProductItem> getProducts() {

        ProductResponse products = restClient.get()
                .uri("/products")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ProductResponse.class);

        assert products != null;
        return products._embedded().productList();
    }

    public MBSAddressResponse addAddress(MappedAddressResponse post) throws JsonProcessingException{
        return restClient.post()
                .uri("/address")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(post))
                .retrieve()
                .body(MBSAddressResponse.class);
    }

    public AddedOrderResponse addToOrder(OrderObject orderObject) throws JsonProcessingException {
        return restClient.post()
                .uri("/create-order?customerId=" + orderObject.customer_id() + "&addressId=" + orderObject.address_id())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(orderObject.product_ids()))
                .retrieve()
                .body(AddedOrderResponse.class);
    }

    public List<HashMap<String, List<ProductItem>>> customerOrders(Long customer_id) throws JsonProcessingException{
        return restClient.get()
                .uri("/customer-orders/" + customer_id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<HashMap<String, List<ProductItem>>>>() {});
    }

    public AddedOrderResponse getOrder(Long orderId) {
        return restClient.get()
                .uri("/order/" + orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AddedOrderResponse.class);
    }

    public PriceResponse orderPrice(Long orderId) {
        return restClient.get()
                .uri("/order-price/" + orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(PriceResponse.class);
    }
}
