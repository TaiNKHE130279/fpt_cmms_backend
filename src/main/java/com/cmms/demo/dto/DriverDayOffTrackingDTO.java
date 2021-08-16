package com.cmms.demo.dto;

import com.cmms.demo.domain.DriverDayOffTracking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDayOffTrackingDTO {
    private Date start_date;
    private Date end_date;
    private String description;
    private String name_request;
    private DriverDTO driver_id;
    private Long id;
    private Date create_date;
    private RequestDayOffStatusDTO status;
    private Long duration;
    public static  DriverDayOffTrackingDTO from(DriverDayOffTracking tracking){
        DriverDayOffTrackingDTO dto = new DriverDayOffTrackingDTO();
        dto.setId(tracking.getId());
        dto.setDriver_id(DriverDTO.from(tracking.getDriver_code()));
        dto.setName_request(tracking.getName_request());
        dto.setDescription(tracking.getDescription());
        dto.setEnd_date(tracking.getEnd_date());
        dto.setStart_date(tracking.getStart_date());
        dto.setStatus(RequestDayOffStatusDTO.from(tracking.getStatus()));
        dto.setCreate_date(tracking.getDate_create());
        dto.setDuration(tracking.getDuration());
        return dto;
    }
}
