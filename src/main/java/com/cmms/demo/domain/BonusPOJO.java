package com.cmms.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity(name = "bonus")
public class BonusPOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long value;
    private boolean isDefault = false;
    @OneToMany(mappedBy = "bonus_id")
    private List<PayrollBonus> bonuses;
    String type;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BonusPOJO bonusPOJO = (BonusPOJO) o;
        return Objects.equals(id, bonusPOJO.id) &&
                Objects.equals(name, bonusPOJO.name) &&
                Objects.equals(value, bonusPOJO.value) &&
                Objects.equals(bonuses, bonusPOJO.bonuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, bonuses);
    }
}
