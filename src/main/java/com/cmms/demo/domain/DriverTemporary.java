package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "driver_temporary")
public class DriverTemporary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Time begin_time;
    private Time finish_time;
    private String driver_code;
    private String machine_code;
    private String evidence_image;
    @OneToOne
    @JoinColumn(name = "schedule_detail_id")
    private BookingScheduleDetail detail;
}
