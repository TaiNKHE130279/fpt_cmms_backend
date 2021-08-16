package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "projects")
public class ProjectPOJO {
    @Id
    private String project_code;
    private String project_name;
    @ManyToOne
    @JoinColumn(name = "customer_code", nullable = false)
    private CustomerPOJO customer;
    private Date expected_starting_date;
    private Date expected_end_date;
    @OneToMany(mappedBy = "project")
    private Set<BookingSchedule> listBookingSchedule = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "status",nullable = false)
    private ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private ProjectType projectType;
}
