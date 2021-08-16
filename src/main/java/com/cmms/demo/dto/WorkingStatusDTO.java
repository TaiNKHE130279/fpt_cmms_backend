package com.cmms.demo.dto;

import com.cmms.demo.domain.WorkingConfirmStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingStatusDTO {
    private Long type_id;
    private String type_name;

    public static WorkingStatusDTO from(WorkingConfirmStatus status){
        WorkingStatusDTO dto = new WorkingStatusDTO();
        dto.setType_id(status.getType_id());
        dto.setType_name(status.getType_name());
        return dto;
    }
}
