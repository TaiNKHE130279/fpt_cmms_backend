package com.cmms.demo.dto;

import com.cmms.demo.domain.CustomerPOJO;
import com.cmms.demo.domain.ProjectPOJO;
import com.cmms.demo.reponsitory.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String customer_code;
    private String name;
    private String phone;
    private Long province_id;
    private Long district_id;
    private Long commune_id;
    private String detail_address;
    private String address;
    private String date_of_birth;
    private String gender;
    private String id_number;
    private List<ProjectDTO> lsProject = new ArrayList<>();

    public static CustomerDTO from(CustomerPOJO item, ProjectRepository projectRepository){
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomer_code(item.getCustomer_code());
        dto.setName(item.getName());
        dto.setPhone(item.getPhone());
        dto.setProvince_id(item.getProvince().getId());
        dto.setDistrict_id(item.getDistrict().getId());
        dto.setCommune_id(item.getCommune().getId());
        dto.setDetail_address(item.getDetail_address());
        if(item.getDetail_address() != null && !item.getDetail_address().equals("")) {
            dto.setAddress(item.getDetail_address() + ", " + item.getCommune().getName()
                    + ", " + item.getDistrict().getName() + ", " + item.getProvince().getName());
        }else{
            dto.setAddress(item.getCommune().getName() + ", " + item.getDistrict().getName()
                    + ", " + item.getProvince().getName());
        }
        dto.setId_number(item.getId_number());
        if(item.getDate_of_birth() != null){
            dto.setDate_of_birth(item.getDate_of_birth().toString());
        }
        dto.setGender(String.valueOf(item.getGender()));
        if(projectRepository != null){
            List<ProjectPOJO> lsProject = projectRepository.getListByCustomer(item.getCustomer_code());
            List<ProjectDTO> lsDto = lsProject.stream().map(ProjectDTO::from).collect(Collectors.toList());
            dto.setLsProject(lsDto);
        }
        return dto;
    }
}
