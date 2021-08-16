package com.cmms.demo.dto;

import com.cmms.demo.domain.PayrollBonus;
import lombok.Data;

@Data
public class PayrollBonusDTO {
    private Long id;
    private Long value;
    private Long quantity = 1L;
    private Long total_value;
    private BonusDTO dto;

    public static PayrollBonusDTO from(PayrollBonus bonus){
        PayrollBonusDTO dto = new PayrollBonusDTO();
        dto.setDto(BonusDTO.from(bonus.getBonus_id()));
        dto.setId(bonus.getId());
        dto.setQuantity(bonus.getQuantity());
        dto.setValue(bonus.getValue());
        dto.setTotal_value(bonus.getTotal_value());
        return dto;
    }
}
