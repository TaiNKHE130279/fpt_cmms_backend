package com.cmms.demo.dto;

import com.cmms.demo.domain.Payroll;
import com.cmms.demo.domain.PayrollDetail;
import com.cmms.demo.domain.PayrollStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PayrollDTO {
    private Long id;
    private String code;
    private String name;
    @NotNull
    private String date_start;
    @NotNull
    private String date_end;
    private String date_create;
    private List<PayrollDetailDTO> detail;
    private Long salary_total;
    private Long salary_paid;
    private Long salary_remain;
    private PayrollStatusDTO status;

    public static PayrollDTO from(Payroll payroll) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        PayrollDTO dto = new PayrollDTO();
        dto.setName(payroll.getName());
        if(payroll.getDetail() != null){
            dto.setDetail(payroll.getDetail().stream().map(PayrollDetailDTO::from).collect(Collectors.toList()));
        }
        dto.setSalary_paid(payroll.getTotal_paid());
        dto.setSalary_total(payroll.getSalary_total());
        dto.setSalary_remain(payroll.getSalary_remain());
        dto.setStatus(PayrollStatusDTO.from(payroll.getStatus()));
        dto.setCode(payroll.getCode());
        dto.setId(payroll.getId());
        dto.setDate_start(payroll.getStart_date().toString());
        dto.setDate_end(payroll.getEnd_date().toString());
        if(payroll.getCreate_date() != null){
            dto.setDate_create(payroll.getCreate_date().toString());
        }
        return dto;
    }
    public static PayrollDTO fromEntityWithOutDetail(Payroll payroll){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        PayrollDTO dto = new PayrollDTO();
        dto.setName(payroll.getName());
//        if(payroll.getDetail() != null){
//            dto.setDetail(payroll.getDetail().stream().map(PayrollDetailDTO::from).collect(Collectors.toList()));
//        }
        dto.setSalary_paid(payroll.getTotal_paid());
        dto.setSalary_total(payroll.getSalary_total());
        dto.setSalary_remain(payroll.getSalary_remain());
        dto.setStatus(PayrollStatusDTO.from(payroll.getStatus()));
        dto.setCode(payroll.getCode());
        dto.setId(payroll.getId());
        dto.setDate_start(payroll.getStart_date().toString());
        dto.setDate_end(payroll.getEnd_date().toString());
        if(payroll.getCreate_date() != null){
            dto.setDate_create(payroll.getCreate_date().toString());
        }
        return dto;
    }
}
