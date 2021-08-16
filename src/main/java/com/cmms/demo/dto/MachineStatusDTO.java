package com.cmms.demo.dto;

import com.cmms.demo.domain.MachineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineStatusDTO {
    private int id;
    private String status_name;

    public static MachineStatusDTO from(MachineStatus item){
        MachineStatusDTO dto = new MachineStatusDTO();
        dto.setId(item.getId());
        dto.setStatus_name(item.getStatus_name());
        return dto;
    }
}
