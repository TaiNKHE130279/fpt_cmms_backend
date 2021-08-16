package com.cmms.demo.dto;

import com.cmms.demo.domain.DriverDayOffTrackingDetailPOJO;
import lombok.Data;

import java.sql.Date;

@Data
public class DriverDayOffTrackingDetailDTO {
    private Long id;
    private Date date;
    private boolean isRemove;
    private String note;
    private String driver_code;

    public static DriverDayOffTrackingDetailDTO from(DriverDayOffTrackingDetailPOJO detailPOJO){
        DriverDayOffTrackingDetailDTO dto = new DriverDayOffTrackingDetailDTO();
        dto.setId(detailPOJO.getId());
        dto.setDate(detailPOJO.getDate());
        dto.setDriver_code(detailPOJO.getDriver_code());
        dto.setNote(detailPOJO.getNote());
        dto.setRemove(detailPOJO.isRemove());
        return dto;
    }
}
