package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamToUpdateDriverInfoDto {
    private String drive_code;
    private String name;
    private Long province_id;
    private Long district_id;
    private Long commune_id;
    private String detail_address;
    private String phone;
    private String driver_image;
    private Long base_salary;
    private String password;
    private String machine_code;
    private Long statusId;
    private String date_of_birth;
    private String gender;
    private String id_number;
    private MultipartFile driver_image_file;
}
