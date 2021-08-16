package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTaskOfDriverDTO {
    private Long schedule_detail_id;
    private String project_code;
    private CustomerDTO customer;
    private String date;
    private String machine_code;
    private String license_plate;
    private String begin_time;
    private String finish_time;
    private Long working_status_id;
    private String working_status_name;
    private String worked_hours;
    private String driver_name;
    private String image;
}
