package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "booking_schedule_detail")
public class BookingScheduleDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "booking_schedule_id",nullable = false)
    private BookingSchedule bookingSchedule;
    private Time begin_time;
    private Time finish_time;
    private String driver_code;
    private String machine_code;
    @Column(columnDefinition="TEXT")
    private String evidence_image;
    @org.hibernate.annotations.Type(type="true_false")
    private Boolean has_temporary;
    private Time worked_hours;
    private Long over_time =0L;
    @ManyToOne
    @JoinColumn(name = "working_confirm_status")
    private WorkingConfirmStatus status;
}
