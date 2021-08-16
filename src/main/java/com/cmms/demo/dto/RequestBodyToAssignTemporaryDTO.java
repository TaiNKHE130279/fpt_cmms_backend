package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyToAssignTemporaryDTO {
    private String project_code;
    private String begin_date;
    private String end_date;
    private String begin_time;
    private String finish_time;
    private String driver_code;
    private String temporary_driver_code;
}
