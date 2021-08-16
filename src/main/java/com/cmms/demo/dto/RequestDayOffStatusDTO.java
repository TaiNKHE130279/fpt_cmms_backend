package com.cmms.demo.dto;

import com.cmms.demo.domain.DayOffRequestStatus;
import lombok.Data;

@Data
public class RequestDayOffStatusDTO {
    private Long id;
    private String name;
    private String description;
    public  static RequestDayOffStatusDTO  from(DayOffRequestStatus status){
        RequestDayOffStatusDTO dto = new RequestDayOffStatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getStatus());
        dto.setDescription(status.getDescription());
        return dto;
    }
}
