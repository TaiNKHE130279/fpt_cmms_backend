package com.cmms.demo.dto;

import com.cmms.demo.domain.DeductionPOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionDTO {
    private Long id;
    private String name;
    private Long value;
    private String unit;
    public static DeductionDTO from(DeductionPOJO pojo){
        DeductionDTO dto = new DeductionDTO();
        dto.setId(pojo.getId());
        dto.setName(pojo.getName());
        dto.setValue(pojo.getValue());
        dto.setUnit(pojo.getType());
        return dto;
    }
}
