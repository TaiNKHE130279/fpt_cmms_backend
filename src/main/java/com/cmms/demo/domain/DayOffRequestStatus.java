package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "request_day_off_status")
public class DayOffRequestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String description;
    @OneToMany(mappedBy = "status",fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private List<DriverDayOffTracking> tracking;

}
