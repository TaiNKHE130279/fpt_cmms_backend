package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "working_confirm_status")
public class WorkingConfirmStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long type_id;
    private String type_name;
    @OneToMany(mappedBy = "status")
    private Set<BookingScheduleDetail> listDetail = new HashSet<>();
    @OneToMany(mappedBy = "status")
    private Set<BookingSchedule> listDate = new HashSet<>();
}
