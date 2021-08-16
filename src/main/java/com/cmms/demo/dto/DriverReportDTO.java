package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverReportDTO {
    private String driver_code;
    private String driver_name;
    private String phone;
    private String address;
    private int number_day_off;
    private String total_work_time;
    private String total_over_time;
    private List<DriverReportDetailDTO> lsDetail = new ArrayList<>();
    private List<String> lsDayOff = new ArrayList<>();
}
