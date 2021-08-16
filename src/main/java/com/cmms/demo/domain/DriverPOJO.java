package com.cmms.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Columns;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "drivers")
public class DriverPOJO {
    @Id
    private String drive_code;
    private String name;
    @JoinColumn(name = "user_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPOJO user;
    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;
    @OneToOne
    @JoinColumn(name = "district_id")
    private District district;
    @OneToOne
    @JoinColumn(name = "commune_id")
    private Commune commune;
    private String detail_address;
    @JoinColumn(unique = true)
    private String phone;
    private Long base_salary;
    @Column(columnDefinition="TEXT")
    private String driver_image;
    private Date date_of_birth;
    private Integer gender;
    private String id_number;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;
    private String address;

    @OneToMany(mappedBy = "driver_code", cascade = CascadeType.ALL)
    private Set<DriverSalaryTracking> trackings = new HashSet<>();
    @OneToMany(mappedBy = "driver_code", cascade = CascadeType.ALL)
    private Set<DriverDayOffTracking> offTrackings = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "machine_code")
    private MachinePOJO machine;

}
