package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDetailByDriverDTO {
    private String date;
    private String begin_time;
    private String finish_time;
    private String driver_code;
    private String machine_code;
}
