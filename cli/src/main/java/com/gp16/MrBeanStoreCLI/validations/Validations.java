package com.gp16.MrBeanStoreCLI.validations;

import java.util.List;

public class Validations {
    public boolean isValidModelId(Integer id, List<Integer> modelIds) {
        return modelIds.contains(id) && id > -1;
    }
}
