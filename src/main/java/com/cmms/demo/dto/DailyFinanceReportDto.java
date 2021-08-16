package com.cmms.demo.dto;

import com.cmms.demo.domain.DailyFinanceReport;
import com.cmms.demo.domain.ProjectPOJO;
import com.cmms.demo.serviceImpl.CustomerServiceImpl;
import com.cmms.demo.serviceImpl.DriverServiceImpl;
import com.cmms.demo.serviceImpl.ProjectServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DailyFinanceReportDto {
    private Long id;
    private String title;
    private Long amount;
    private Date payment_date;
    private String note;
    private PaymentTypeDto payment_type;
    private CustomerDTO customer;
    private String project_code;
    private DriverDTO driver;
    private boolean is_auto;

    public static DailyFinanceReportDto from(DailyFinanceReport dailyFinanceReport
            , ProjectServiceImpl projectService, DriverServiceImpl driverServiceImpl){
        DailyFinanceReportDto dto = new DailyFinanceReportDto();
        dto.setTitle(dailyFinanceReport.getTitle());
        dto.setId(dailyFinanceReport.getId());
        dto.setTitle(dailyFinanceReport.getTitle());
        dto.setAmount(dailyFinanceReport.getAmount());
        dto.setPayment_date(dailyFinanceReport.getPayment_date());
        dto.setNote(dailyFinanceReport.getNote());
        dto.setPayment_type(PaymentTypeDto.from(dailyFinanceReport.getPaymentType()));
        if(projectService != null && dailyFinanceReport.getProject_code() != null){
            ProjectPOJO project = projectService.getOne(dailyFinanceReport.getProject_code());
            dto.setProject_code(project.getProject_code());
            dto.setCustomer(CustomerDTO.from(project.getCustomer(), null));
        }
        if(driverServiceImpl != null && dailyFinanceReport.getDriver_code() != null){
            dto.setDriver(DriverDTO.from(driverServiceImpl.getOne(dailyFinanceReport.getDriver_code())));
        }
        dto.set_auto(dailyFinanceReport.is_auto());
        return dto;
    }
}
