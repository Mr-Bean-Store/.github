package com.gp16.MrBeanStoreCLI.models.response.MBS;

import java.sql.Timestamp;

public record AddedOrderResponse(
        Long orderId,
        CustomerResponse customer,
        MBSAddressResponse delivery,
        Timestamp orderDate,
        Timestamp arrivalDate
) {
}
