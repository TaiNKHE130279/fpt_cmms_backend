package com.cmms.demo.dto;

import com.cmms.demo.domain.DeductionPOJO;
import com.cmms.demo.domain.Payroll;
import com.cmms.demo.domain.PayrollDetail;
import com.cmms.demo.domain.UserPOJO;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PayrollDetailDTO {
    Long id;
    private DriverDTO driver;
    private List<PayrollBonusDTO> bonus;
    private Long number_day_off;
    private Long actually_received;
    private Long payroll_id;
    private List<PayrollDeductionDTO> deduction;
    private Long total_bonus;
    private Long total_deduction;
    private Long total_paid;
    private Long salary_remain;
    private Long nominal_income;
    private Long worked_day;
    private Long total_over_time;

    public static PayrollDetailDTO from(PayrollDetail detail) {
        PayrollDetailDTO dto = new PayrollDetailDTO();
        dto.setDriver(DriverDTO.from(detail.getUser_id().getDriver()));
        dto.setNumber_day_off(detail.getNumber_day_off());
        dto.setId(detail.getId());
        dto.setBonus(detail.getBonus().stream().map(PayrollBonusDTO::from).collect(Collectors.toList()));
        dto.setActually_received(detail.getActually_received());
        dto.setPayroll_id(detail.getPayroll().getId());
        dto.setTotal_paid(detail.getPaid_staff());
        dto.setSalary_remain(detail.getSalary_remain());
        dto.setDeduction(detail.getDeduction().stream().map(PayrollDeductionDTO::from).collect(Collectors.toList()));
        dto.setTotal_bonus(detail.getBonus().stream().map(bonusPOJO -> bonusPOJO.getValue() * bonusPOJO.getQuantity()).reduce(0L, Long::sum));
        dto.setTotal_deduction(detail.getDeduction().stream().map(pojo -> pojo.getValue() * pojo.getQuantity()).reduce(0L, Long::sum));
        dto.setNominal_income(detail.getNominal_income());
        dto.setWorked_day(detail.getWorked_day());
        dto.setTotal_over_time(detail.getTotal_over_time());
        return dto;
    }
}
