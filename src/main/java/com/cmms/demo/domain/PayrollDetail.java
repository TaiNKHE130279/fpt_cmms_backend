package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payroll_detail")
public class PayrollDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPOJO user_id;
    @OneToMany(mappedBy = "payroll_detail_id")
    private List<PayrollBonus> bonus;
    private Long number_day_off;
    private Long actually_received;
    @OneToMany(mappedBy = "payroll_detail_id")
    private List<PayrollDeduction> deduction;
    private Long paid_staff;
    private Long salary_remain;
    private Long worked_day;
    @ManyToOne
    @JoinColumn
    private Payroll payroll;
    private boolean isPayment = false;
    private boolean isRemove = false;
    private Long nominal_income;
    private Long total_over_time =0L;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayrollDetail detail = (PayrollDetail) o;
        return isPayment == detail.isPayment &&
                Objects.equals(id, detail.id) &&
                Objects.equals(user_id, detail.user_id) &&
                Objects.equals(bonus, detail.bonus) &&
                Objects.equals(number_day_off, detail.number_day_off) &&
                Objects.equals(actually_received, detail.actually_received) &&
                Objects.equals(deduction, detail.deduction) &&
                Objects.equals(payroll, detail.payroll);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, bonus, number_day_off, actually_received, deduction, payroll, isPayment);
    }
}
