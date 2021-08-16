package com.cmms.demo.dto;

import com.cmms.demo.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusDTO {
    private Long status_id;
    private String name;

    public static PaymentStatusDTO from (PaymentStatus paymentStatus){
        PaymentStatusDTO dto = new PaymentStatusDTO();
        dto.setName(paymentStatus.getName());
        dto.setStatus_id(paymentStatus.getStatus_id());
        return dto;
    }
}
