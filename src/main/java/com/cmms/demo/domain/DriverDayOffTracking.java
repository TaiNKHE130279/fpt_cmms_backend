package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "driver_day_off_tracking")
public class DriverDayOffTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "driver_code")
    private DriverPOJO driver_code;
    private Date start_date;
    String name_request;
    private String description;
    private Date end_date;
    private long duration;
    private Date date_create;
    private boolean isProcessed =false;
    private boolean isApproved = false;
    private long timeRemain = 0;
    private boolean isStarted = false;
    @ManyToOne
    @JoinColumn(name = "request_status")
    private DayOffRequestStatus status;

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
