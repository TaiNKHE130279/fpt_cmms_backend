package com.cmms.demo.dto;

import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.DriverTemporary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDetailDTO {
    private Long schedule_detail_id;
    private String begin_time;
    private String finish_time;
    private String driver_code;
    private String driver_name;
    private String driver_phone;
    private String machine_code;
    private String license_plate;
    private String evidence_image;
    private Long working_status;
    private String working_status_name;
    private String worked_hour;
    private Boolean has_temporary;


    public static ScheduleDetailDTO from(BookingScheduleDetail item) {
        ScheduleDetailDTO dto = new ScheduleDetailDTO();
        DateFormat format = new SimpleDateFormat("HH:mm");
        if (item.getId() != null) {
            dto.setSchedule_detail_id(item.getId());
        }
        dto.setBegin_time(format.format(item.getBegin_time()));
        dto.setFinish_time(format.format(item.getFinish_time()));
        dto.setDriver_code(item.getDriver_code());
        dto.setMachine_code(item.getMachine_code());
        dto.setEvidence_image(item.getEvidence_image());
        dto.setWorking_status(item.getStatus().getType_id());
        dto.setHas_temporary(item.getHas_temporary());
        dto.setWorking_status_name(item.getStatus().getType_name());
        if(item.getWorked_hours() != null) {
            dto.setWorked_hour(format.format(item.getWorked_hours()));
        }
        return dto;
    }
}
