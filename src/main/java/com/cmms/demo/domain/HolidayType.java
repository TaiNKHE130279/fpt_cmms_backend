package com.cmms.demo.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "holiday_type")
public class HolidayType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type_name;
    @OneToMany(mappedBy = "type")
    private Set<Holiday> holidaySet = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Set<Holiday> getHolidaySet() {
        return holidaySet;
    }

    public void setHolidaySet(Set<Holiday> holidaySet) {
        this.holidaySet = holidaySet;
    }
}
