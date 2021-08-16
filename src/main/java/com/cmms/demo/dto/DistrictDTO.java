package com.cmms.demo.dto;

import com.cmms.demo.domain.District;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDTO {
    private Long id;
    private String name;
    private String rank;
    private Long province_id;

    public static DistrictDTO from(District d){
        DistrictDTO dto = new DistrictDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setRank(d.getRank());
        dto.setProvince_id(d.getProvince().getId());
        return dto;
    }
}
