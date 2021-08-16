package com.cmms.demo.dto;

import com.cmms.demo.domain.PaymentType;

public class PaymentTypeDto {
    private int type_id;
    private String type_name;

    public static PaymentTypeDto from(PaymentType type){
        PaymentTypeDto dto = new PaymentTypeDto();
        dto.setType_id(type.getType_id());
        dto.setType_name(type.getType_name());
        return dto;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
