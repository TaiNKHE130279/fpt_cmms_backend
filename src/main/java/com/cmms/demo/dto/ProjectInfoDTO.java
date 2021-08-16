package com.cmms.demo.dto;

import com.cmms.demo.domain.BookingSchedule;
import com.cmms.demo.domain.BookingScheduleDetail;
import com.cmms.demo.domain.ProjectPOJO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoDTO {
    private String project_code;
    private String project_name;
    private CustomerDTO customer;
    private String date;
    private String project_status;
    private String project_type;
    private List<ScheduleDetailDTO> listDriverInfo = new ArrayList<>();
}
