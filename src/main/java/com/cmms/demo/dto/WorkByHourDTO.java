package com.cmms.demo.dto;

import com.cmms.demo.domain.WorkByHourPOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkByHourDTO {
    private Long id;
    private String project_code;
    private Date create_date;
    private Long price_per_hour;

    public static WorkByHourDTO from(WorkByHourPOJO workByHourPOJO){
            WorkByHourDTO dto = new WorkByHourDTO();
            dto.setId(workByHourPOJO.getId());
            dto.setCreate_date(workByHourPOJO.getCreate_date());
            dto.setProject_code(workByHourPOJO.getProject().getProject_code());
            dto.setPrice_per_hour(workByHourPOJO.getPrice_per_hour());
            return dto;
    }
}
