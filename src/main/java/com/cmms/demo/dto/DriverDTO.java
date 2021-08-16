package com.cmms.demo.dto;

import com.cmms.demo.domain.DriverPOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {
    private String drive_code;
    private String name;
    private String account;
    private Long province_id;
    private Long district_id;
    private Long commune_id;
    private String detail_address;
    private String phone;
    private Long base_salary;
    private String driver_image;
    private String machine_code;
    private String license_plate;
    private String address;
    private String date_of_birth;
    private String gender;
    private String id_number;
    private MultipartFile driver_image_file;

    public static DriverDTO from (DriverPOJO driver){
        DriverDTO dto = new DriverDTO();
        dto.setDrive_code(driver.getDrive_code());
        dto.setName(driver.getName());
        dto.setProvince_id(driver.getProvince().getId());
        dto.setDistrict_id(driver.getDistrict().getId());
        dto.setCommune_id(driver.getCommune().getId());
        dto.setDetail_address(driver.getDetail_address());
        dto.setPhone(driver.getPhone());
        dto.setBase_salary(driver.getBase_salary());
        dto.setDriver_image(driver.getDriver_image());
        if(driver.getMachine() != null) {
            dto.setMachine_code(driver.getMachine().getMachine_code());
            dto.setLicense_plate(driver.getMachine().getLicense_plate());
        }else{
            dto.setMachine_code(null);
            dto.setLicense_plate(null);
        }
        dto.setAccount(driver.getUser().getAccount());
        if(driver.getDetail_address() != null) {
            dto.setAddress(driver.getDetail_address() + ", " + driver.getCommune().getName()
                    + ", " + driver.getDistrict().getName() + ", " + driver.getProvince().getName());
        }else{
            dto.setAddress(driver.getCommune().getName()+ ", " + driver.getDistrict().getName()
                    + ", " + driver.getProvince().getName());
        }
        dto.setId_number(driver.getId_number());
        if(driver.getDate_of_birth() != null){
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            dto.setDate_of_birth(driver.getDate_of_birth().toString());
        }
        dto.setGender(String.valueOf(driver.getGender()));
        return dto;
    }
}
