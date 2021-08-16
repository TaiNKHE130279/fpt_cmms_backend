package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamToUpdateScheduleDetailDTO {
    private String project_code;
    private String date;
    private Long schedule_detail_id;
    private String begin_time;
    private String finish_time;
    private String driver_code;
}
