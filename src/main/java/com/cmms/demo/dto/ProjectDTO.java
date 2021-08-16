package com.cmms.demo.dto;

import com.cmms.demo.domain.ProjectPOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private String project_code;
    private String project_name;
    private CustomerDTO customer;
    private String expected_starting_date;
    private String expected_end_date;
    private Long project_type;
    private Integer project_status;
   private ProjectTypeDTO projectTypeDTO;
   private ProjectStatusDTO projectStatusDTO;


    public static ProjectDTO from(ProjectPOJO item){
        ProjectDTO dto = new ProjectDTO();
        dto.setProject_code(item.getProject_code());
        dto.setProject_name(item.getProject_name());
        dto.setCustomer(CustomerDTO.from(item.getCustomer(), null));

        if(item.getExpected_starting_date() != null) {
            dto.setExpected_starting_date(item.getExpected_starting_date().toString());
        }
        if(item.getExpected_end_date() != null) {
            dto.setExpected_end_date(item.getExpected_end_date().toString());
        }

        dto.setProjectTypeDTO(ProjectTypeDTO.from(item.getProjectType()));

        dto.setProjectStatusDTO(ProjectStatusDTO.from(item.getProjectStatus()));
        return dto;
    }
}
