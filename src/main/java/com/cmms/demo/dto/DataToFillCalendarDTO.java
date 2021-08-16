package com.cmms.demo.dto;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.serviceImpl.ContractServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataToFillCalendarDTO {
    private Date date;
    private List<ProjectAndCustomerInfoDTO> listProject = new ArrayList<>();

    public static DataToFillCalendarDTO from(Date date, List<BookingSchedule> ls, ContractServiceImpl service){
        DataToFillCalendarDTO dto = new DataToFillCalendarDTO();
        dto.setDate(date);
        List<ProjectAndCustomerInfoDTO> lsInfo = new ArrayList<>();
        for(int i = 0; i<ls.size(); i++){
            BookingSchedule b = ls.get(i);
            ProjectAndCustomerInfoDTO info = new ProjectAndCustomerInfoDTO();
            info.setProject_code(b.getProject().getProject_code());
            info.setCustomer(CustomerDTO.from(b.getProject().getCustomer(), null));
            info.setProject_status(b.getProject().getProjectStatus().getStatus_name());
            info.setProject_type(ProjectTypeDTO.from(b.getProject().getProjectType()));
            if(b.getProject().getProjectType().getType_id() == 2){
                if(service.getByProjectCode(b.getProject().getProject_code())!=null){
                    info.setHas_contract(1);
                }else{
                    info.setHas_contract(0);
                }
            }
            info.setStatus_name(b.getStatus().getType_name());
            lsInfo.add(info);
        }
        dto.setListProject(lsInfo);
        return dto;
    }
}
