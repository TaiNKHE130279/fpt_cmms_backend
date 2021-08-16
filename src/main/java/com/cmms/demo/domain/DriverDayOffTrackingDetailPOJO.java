package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "driver_dayoff_detail")
public class DriverDayOffTrackingDetailPOJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String driver_code;
    private Date date;
    private String note;
    private boolean isRemove = false;
    private boolean isAwol = false;
}
