package com.cmms.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyToAssignDriverDTO {
    private String project_code;
    private String begin_date;
    private String end_date;
    private String begin_time;
    private String finish_time;
    private List<ListDriverDTO> listDriver = new ArrayList<>();
}
