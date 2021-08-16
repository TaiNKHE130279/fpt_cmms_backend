package com.cmms.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity(name = "deduction")
public class DeductionPOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long value;
    @OneToMany(mappedBy = "deduction_id")
    List<PayrollDeduction> deductions;
    private boolean isDefault;
    String type;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeductionPOJO that = (DeductionPOJO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value) &&
                Objects.equals(deductions, that.deductions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, deductions);
    }
}
