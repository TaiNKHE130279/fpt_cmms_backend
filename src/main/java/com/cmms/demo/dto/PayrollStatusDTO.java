package com.cmms.demo.dto;

import com.cmms.demo.domain.PayrollStatus;
import lombok.Data;

@Data
public class PayrollStatusDTO {
    private String name;
    private Long id;

    public static PayrollStatusDTO from(PayrollStatus status){
        PayrollStatusDTO dto = new PayrollStatusDTO();
        dto.setName(status.getName());
        dto.setId(status.getId());
        return dto;
    }

}
