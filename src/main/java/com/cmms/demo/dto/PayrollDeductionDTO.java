package com.cmms.demo.dto;

import com.cmms.demo.domain.PayrollDeduction;
import lombok.Data;

@Data
public class PayrollDeductionDTO {
    private DeductionDTO dto;
    private Long id;
    private Long quantity;
    private Long value;
    private Long total_value;

    public static PayrollDeductionDTO from(PayrollDeduction deduction){
        PayrollDeductionDTO dto = new PayrollDeductionDTO();
        dto.setDto(DeductionDTO.from(deduction.getDeduction_id()));
        dto.setId(deduction.getId());
        dto.setQuantity(deduction.getQuantity());
        dto.setValue(deduction.getValue());
        dto.setTotal_value(deduction.getTotal_value());
        return dto;
    }
}
