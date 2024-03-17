package com.gp16.MrBeanStoreCLI.models.response.MBS;

import java.util.List;

public record GeometryResponse(
        CoordinateResponse geometry,
        String place_name
) {
}
