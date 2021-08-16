package com.cmms.demo.dto;

import com.cmms.demo.domain.BonusPOJO;
import lombok.Data;

@Data
public class BonusDTO {
    private Long id;
    private String name;
    private Long value;
    private String unit;
    public static BonusDTO from(BonusPOJO pojo){
        BonusDTO dto = new BonusDTO();
        dto.setId(pojo.getId());
        dto.setName(pojo.getName());
        dto.setValue(pojo.getValue());
        dto.setUnit(pojo.getType());
        return dto;
    }
}
