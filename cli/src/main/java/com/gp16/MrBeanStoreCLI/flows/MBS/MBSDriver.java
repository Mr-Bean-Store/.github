package com.gp16.MrBeanStoreCLI.flows.MBS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gp16.MrBeanStoreCLI.models.response.MBS.CustomerResponse;
import com.gp16.MrBeanStoreCLI.services.MBS.MBSService;
import org.springframework.stereotype.Component;

@Component
public class MBSDriver {
    MBSService mbsService;

    public MBSDriver(MBSService mbsService) {
        this.mbsService = mbsService;
    }

    public CustomerResponse registerCustomer(String firstname, String lastname, String email) throws JsonProcessingException {
        return mbsService.registerCustomer(firstname, lastname, email);
    }
//
//    public String getProducts() {
//        return mbsService.getProducts();
//    }
}
