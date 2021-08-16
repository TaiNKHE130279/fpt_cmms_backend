package com.cmms.demo.dto;

import com.cmms.demo.domain.Province;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceDTO {
    private Long id;
    private String name;
    private String rank;

    public static ProvinceDTO from(Province p){
        ProvinceDTO dto = new ProvinceDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setRank(p.getRank());
        return dto;
    }
}
