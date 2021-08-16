package com.cmms.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity(name = "payroll_deduction")
public class PayrollDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "deduction_id")
    private DeductionPOJO deduction_id;


    @ManyToOne
    @JoinColumn(name = "payroll_detail_id")
    private PayrollDetail payroll_detail_id;

    private Long quantity;
    private Long value;
    private Long total_value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayrollDeduction that = (PayrollDeduction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(deduction_id, that.deduction_id) &&
                Objects.equals(payroll_detail_id, that.payroll_detail_id) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(value, that.value) &&
                Objects.equals(total_value, that.total_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deduction_id, payroll_detail_id, quantity, value, total_value);
    }
}
