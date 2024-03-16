package com.gp16.MrBeanStoreCLI.api;

import com.gp16.MrBeanStoreCLI.models.response.MBS.AddressResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.GeometryResponse;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MappedAddressResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class AddressHttpAPIHandler {
    RestClient restClient;
    private final String token = "pk.eyJ1Ijoid2x0cm1nZ2wiLCJhIjoiY2x0cjFxNmR4MGJtMjJrbzN6NGJ4M2lsaCJ9.6o8QYYQFM2rdqah5ROJYlA";

    public AddressHttpAPIHandler() {
        restClient = RestClient.builder()
                .baseUrl("https://api.mapbox.com/geocoding/v5")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            throw new RestClientException(response.toString());
                        })
                )
                .build();
    }

    public MappedAddressResponse mapAddress(String address) {
        AddressResponse res = restClient.get()
                .uri("/mapbox.places/" + address + ".json?access_token=" + token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AddressResponse.class);

        assert res != null;
        List<GeometryResponse> features = res.features();
        GeometryResponse feature = features.getFirst();
        List<Double> coordinates = feature.geometry().coordinates();

        return new MappedAddressResponse(coordinates.get(1), coordinates.get(0));
    }
}
