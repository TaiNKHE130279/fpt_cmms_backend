package com.cmms.demo.dto;

import com.cmms.demo.domain.DayOffRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayOffRequestDTO {
    private Long id;
    private String name;

    public static DayOffRequestDTO from(DayOffRequest request){
        DayOffRequestDTO dto = new DayOffRequestDTO();
        dto.setId(request.getId());
        dto.setName(request.getName_request());
        return dto;
    }

}
