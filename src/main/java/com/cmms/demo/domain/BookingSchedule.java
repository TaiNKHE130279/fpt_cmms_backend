package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "booking_schedule")
public class BookingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "project_code",nullable = false)
    private ProjectPOJO project;
    private Date date;
    @OneToMany(mappedBy = "bookingSchedule")
    private Set<BookingScheduleDetail> listBookingDetail;
    @ManyToOne
    @JoinColumn(name = "working_confirm_status")
    private WorkingConfirmStatus status;
}
