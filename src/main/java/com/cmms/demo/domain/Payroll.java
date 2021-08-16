package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    @NotNull
    private Date start_date;
    @NotNull
    private Date end_date;
    @OneToMany(mappedBy = "payroll",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<PayrollDetail> detail;
    private Long salary_total;
    private Long total_paid;
    private Long salary_remain;
    private Date create_date;
    @ManyToOne
    @JoinColumn
    private PayrollStatus status;
    private boolean isClosing = false;
    private boolean isDone= false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payroll payroll = (Payroll) o;
        return isClosing == payroll.isClosing &&
                isDone == payroll.isDone &&
                Objects.equals(id, payroll.id) &&
                Objects.equals(code, payroll.code) &&
                Objects.equals(name, payroll.name) &&
                Objects.equals(start_date, payroll.start_date) &&
                Objects.equals(end_date, payroll.end_date) &&
                Objects.equals(detail, payroll.detail) &&
                Objects.equals(salary_total, payroll.salary_total) &&
                Objects.equals(salary_remain, payroll.salary_remain) &&
                Objects.equals(status, payroll.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, start_date, end_date, detail, salary_total, salary_remain, status, isClosing, isDone);
    }


}
