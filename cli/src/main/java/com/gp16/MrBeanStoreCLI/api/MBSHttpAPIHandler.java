package com.gp16.MrBeanStoreCLI.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gp16.MrBeanStoreCLI.models.posts.MBS.*;
import com.gp16.MrBeanStoreCLI.models.response.MBS.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class MBSHttpAPIHandler {
    RestClient restClient;
    ObjectMapper objectMapper = new ObjectMapper();

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

    public void addToOrder(OrderObject orderObject) throws JsonProcessingException {
        restClient.post()
                .uri("/createOrderFromProducts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(orderObject))
                .retrieve()
                .toBodilessEntity();
    }
}
