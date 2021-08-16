package com.cmms.demo.dto;

import com.cmms.demo.domain.DriverPOJO;
import com.cmms.demo.domain.DriverTemporary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverTemporaryDTO {
    private Long id;
    private Time begin_time;
    private Time finish_time;
    private String driver_code;
    private String driver_name;
    private String driver_phone;
    private String machine_code;
    private String license_plate;
    private String evidence_image;

    public static DriverTemporaryDTO from(DriverTemporary item){
        DriverTemporaryDTO dto = new DriverTemporaryDTO();
        if(item != null) {
            dto.setId(item.getId());
            dto.setBegin_time(item.getBegin_time());
            dto.setFinish_time(item.getFinish_time());
            dto.setDriver_code(item.getDriver_code());
            dto.setMachine_code(item.getMachine_code());
            dto.setEvidence_image(item.getEvidence_image());
            return dto;
        }
        return null;
    }
}
