package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAndCustomerInfoDTO {
    private String project_code;
    private CustomerDTO customer;
    private String project_status;
    private ProjectTypeDTO project_type;
    private int has_contract;
    private String status_name;
}
