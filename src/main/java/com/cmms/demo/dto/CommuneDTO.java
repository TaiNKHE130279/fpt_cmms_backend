package com.cmms.demo.dto;

import com.cmms.demo.domain.Commune;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommuneDTO {
    private Long id;
    private String name;
    private String rank;
    private Long district_id;

    public static CommuneDTO from(Commune d){
        CommuneDTO dto = new CommuneDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setRank(d.getRank());
        dto.setDistrict_id(d.getDistrict().getId());
        return dto;
    }
}
