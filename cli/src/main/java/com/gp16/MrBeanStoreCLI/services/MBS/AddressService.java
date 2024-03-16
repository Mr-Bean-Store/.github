package com.gp16.MrBeanStoreCLI.services.MBS;

import com.gp16.MrBeanStoreCLI.api.AddressHttpAPIHandler;
import com.gp16.MrBeanStoreCLI.models.response.MBS.MappedAddressResponse;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    AddressHttpAPIHandler addressHttpAPIHandler;

    public AddressService(AddressHttpAPIHandler addressHttpAPIHandler) {
        this.addressHttpAPIHandler = addressHttpAPIHandler;
    }

    public MappedAddressResponse mapAddress(String address) {
        return addressHttpAPIHandler.mapAddress(address);
    }
}