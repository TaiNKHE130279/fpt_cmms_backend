package com.cmms.demo.dto;

import com.cmms.demo.domain.ProjectPOJO;
import com.cmms.demo.serviceImpl.ContractServiceImpl;
import com.cmms.demo.serviceImpl.InvoiceServiceImpl;
import com.cmms.demo.serviceImpl.ScheduleDetailServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDTO {
    private String project_code;
    private String project_name;
    private CustomerDTO customer;
    private ProjectTypeDTO project_type;
    private ProjectStatusDTO project_status;
    private String folder_id;
    private boolean has_contract;
    private boolean has_invoice;
    private boolean has_complete_btn;

    public static ProjectResponseDTO from(ProjectPOJO p, ContractServiceImpl contractService, InvoiceServiceImpl invoiceService
            , ScheduleDetailServiceImpl detailService){
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setProject_code(p.getProject_code());
        dto.setProject_name(p.getProject_name());
        dto.setCustomer(CustomerDTO.from(p.getCustomer(), null));
        dto.setProject_type(ProjectTypeDTO.from(p.getProjectType()));
        dto.setProject_status(ProjectStatusDTO.from(p.getProjectStatus()));
        if(p.getProjectType().getType_id() == 2){
            if(contractService.getByProjectCode(p.getProject_code()) != null){
                dto.setHas_contract(true);
            }else{
                dto.setHas_contract(false);
            }
        }
        if(invoiceService.getByProjectCode(p.getProject_code()) != null){
            dto.setHas_invoice(true);
        }else{
            dto.setHas_invoice(false);
        }
        if(detailService.checkHasWorkTime(p.getProject_code()) && p.getProjectStatus().getId() == 2){
            dto.setHas_complete_btn(true);
        }else{
            dto.setHas_complete_btn(false);
        }
        return dto;
    }
}
