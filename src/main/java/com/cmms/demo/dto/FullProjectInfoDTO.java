package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullProjectInfoDTO {
    private int totalPages;
    private String project_code;
    private String project_name;
    private CustomerDTO customer;
    private String project_status;
    private String project_type;
    private List<ListDateInfoDTO> lsDate = new ArrayList<>();
    private boolean has_contract;
    private boolean has_invoice;

}
